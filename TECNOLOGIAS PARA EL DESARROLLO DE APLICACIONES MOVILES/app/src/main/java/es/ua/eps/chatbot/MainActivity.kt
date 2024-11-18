package es.ua.eps.chatbot

import android.content.Intent
import android.graphics.Color
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.chatbot.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
    private var serverIP: String = "192.168.100.12"
    private var serverPort: Int = 6000
    private val IMAGE_PICK_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.targetAddress.setText(serverIP)
        binding.port.setText(serverPort.toString())

        binding.newSocketButton.setOnClickListener {
            val targetAddress = binding.targetAddress.text.toString()
            val port = binding.port.text.toString()
            if (targetAddress.isNotBlank() && port.isNotBlank()) {
                serverIP = targetAddress
                serverPort = port.toIntOrNull() ?: 0
                if (serverPort > 0 && esDireccionIPv4Valida(targetAddress)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        closeConnection()
                        connectToServer()
                    }
                } else {
                    showToast("Dirección o puerto inválido.")
                }
            } else {
                showToast("Por favor, ingresa una dirección y un puerto.")
            }
        }

        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    sendTextMessage(message)
                }
            } else {
                showToast("El mensaje no puede estar vacío")
            }
        }

        binding.closePortButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                closeConnection()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val uri = data?.data
            val filePath = uri?.let { uriPath ->
                val cursor = contentResolver.query(uriPath, null, null, null, null)
                cursor?.let {
                    it.moveToFirst()
                    val index = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    val path = it.getString(index)
                    it.close()
                    path
                }
            }

            filePath?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    sendImage(File(it))
                }
            }
        }
    }

    private fun connectToServer() {
        try {
            socket?.close()
            socket = Socket(serverIP, serverPort)
            output = PrintWriter(socket!!.getOutputStream(), true)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream()))

            runOnUiThread {
                binding.connectionStatus.text = "Conectado a $serverIP:$serverPort"
                showToast("Conexión establecida")
            }

            CoroutineScope(Dispatchers.IO).launch {
                listenForMessages()
            }
        } catch (e: IOException) {
            runOnUiThread {
                binding.connectionStatus.text = "Conexión fallida"
                showToast("Error al conectar: ${e.message}")
            }
        }
    }

    private fun listenForMessages() {
        try {
            var message: String?
            while (input?.readLine().also { message = it } != null) {
                if (message!!.startsWith("TEXT:")) {
                    val textMessage = message!!.removePrefix("TEXT:")
                    runOnUiThread {
                        binding.messageContainer.addView(createMessageTextView(textMessage, isMine = false))
                    }
                } else if (message!!.startsWith("IMAGE:")) {
                    val filePath = applicationContext.filesDir.absolutePath + "/received_image.jpg"
                    val file = File(filePath)
                    val outputStream = FileOutputStream(file)
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (socket!!.getInputStream().read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    outputStream.close()

                    runOnUiThread {
                        val bitmap = BitmapFactory.decodeFile(filePath)
                        binding.messageContainer.addView(createImageView(bitmap))
                    }
                }
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error en la conexión: ${e.message}")
            }
        }
    }

    private fun sendTextMessage(message: String) {
        try {
            output?.println("TEXT:$message")
            runOnUiThread {
                binding.messageContainer.addView(createMessageTextView("Tú: $message", isMine = true))
                binding.messageInput.text.clear()
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al enviar el mensaje: ${e.message}")
            }
        }
    }

    private fun sendImage(imageFile: File) {
        try {
            val outputStream = socket!!.getOutputStream()
            output?.println("IMAGE:")
            val fileInputStream = FileInputStream(imageFile)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
            fileInputStream.close()
        } catch (e: Exception) {
            runOnUiThread {
                showToast("Error al enviar imagen: ${e.message}")
            }
        }
    }

    private fun closeConnection() {
        try {
            socket?.close()
            socket = null
            runOnUiThread {
                binding.connectionStatus.text = "Desconectado"
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al cerrar la conexión: ${e.message}")
            }
        }
    }

    private fun createMessageTextView(message: String, isMine: Boolean): TextView {
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = if (isMine) Gravity.END else Gravity.START
        textView.layoutParams = params
        textView.text = message
        textView.setPadding(16, 16, 16, 16)
        textView.setBackgroundResource(if (isMine) R.drawable.bocadillo_salida else R.drawable.bocadillo_entrada)
        textView.setTextColor(Color.BLACK)
        return textView
    }

    private fun createImageView(bitmap: android.graphics.Bitmap): android.widget.ImageView {
        val imageView = android.widget.ImageView(this)
        imageView.setImageBitmap(bitmap)
        return imageView
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun esDireccionIPv4Valida(ip: String): Boolean {
        val partes = ip.split(".")
        return partes.size == 4 && partes.all { it.toIntOrNull() in 0..255 }
    }

    override fun onDestroy() {
        super.onDestroy()
        closeConnection()
    }
}
