package es.ua.eps.chatbot

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
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
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null
    private var serverIP: String = "192.168.100.12"
    private var serverPort: Int = 6000
    private var aliasClient: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.targetAddress.setText(serverIP)
        binding.port.setText(serverPort.toString())

        binding.newSocketButton.setOnClickListener {
            val targetAddress = binding.targetAddress.text.toString()
            aliasClient = binding.alias.text.toString()

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

    fun cifrarTexto(texto: String, claveSecreta: String): String {
        try {
            // Convierte la clave secreta en un formato adecuado
            val secretKey = SecretKeySpec(claveSecreta.toByteArray(Charsets.UTF_8), "AES")

            // Inicializa el cifrador
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            // Cifra el texto
            val textoCifrado = cipher.doFinal(texto.toByteArray(Charsets.UTF_8))

            // Convierte el texto cifrado a una cadena en base64 para enviarlo fácilmente
            return Base64.encodeToString(textoCifrado, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun descifrarTexto(textoCifrado: String, claveSecreta: String): String {
        try {
            // Convierte la clave secreta en un formato adecuado
            val secretKey = SecretKeySpec(claveSecreta.toByteArray(Charsets.UTF_8), "AES")

            // Inicializa el cifrador
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)

            // Decodifica el texto cifrado de base64
            val textoCifradoBytes = Base64.decode(textoCifrado, Base64.DEFAULT)

            // Descifra el texto
            val textoDescifrado = cipher.doFinal(textoCifradoBytes)

            // Convierte el texto descifrado de bytes a String
            return String(textoDescifrado, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
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
                // La IP ya está incluida en el mensaje por el servidor
                runOnUiThread {
                    var mensaje = descifrarTexto(message!!, claveSecreta = "clavesecreta1234")
                    binding.messageContainer.addView(createMessageTextView(mensaje!!, isMine = false))
                }
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error en la conexión: ${e.message}")
            }
        }
    }

    fun sendTextMessage(message: String) {
        val mensajeCifrado = cifrarTexto("$aliasClient: $message", "clavesecreta1234") //Clave secreta
        try {
            output?.println(mensajeCifrado)  // Enviar el mensaje cifrado
            output?.flush()
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

        params.topMargin = 16
        params.bottomMargin = 16
        if (isMine) {
            params.leftMargin = 150
        }else {
            params.rightMargin = 150
        }

        textView.layoutParams = params

        textView.text = message
        textView.setPadding(16, 16, 16, 16)
        textView.setBackgroundResource(
            if (isMine) {
                R.drawable.bocadillo_salida
            } else {
                R.drawable.bocadillo_entrada
            })
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
        if (partes.size != 4) return false

        for (parte in partes) {
            val numero = parte.toIntOrNull() ?: return false
            if (numero < 0 || numero > 255) return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        closeConnection()
    }
}
