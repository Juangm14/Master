package es.ua.eps.chatbot

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.view.marginBottom
import es.ua.eps.chatbot.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var socket: Socket? = null
    private var output: PrintWriter? = null
    private var input: BufferedReader? = null

    private var serverIP: String = "192.168.100.12"
    private var serverPort: Int = 6000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.targetAddress.setText(serverIP)
        binding.port.setText((serverPort ?: 0).toString())

        // Botón para crear un nuevo socket
        binding.newSocketButton.setOnClickListener {
            val targetAddress = binding.targetAddress.text.toString()
            val port = binding.port.text.toString()

            if (targetAddress.isNotBlank() && port.isNotBlank()) {
                serverIP = targetAddress
                serverPort = port.toIntOrNull() ?: 0

                if (serverPort > 0 && esDireccionIPv4Valida(targetAddress)) {
                    // Cierra la conexión anterior si existe
                    CoroutineScope(Dispatchers.IO).launch {
                        closeConnection()
                        connectToServer()
                    }
                } else {
                    if(!esDireccionIPv4Valida(targetAddress)){
                        showToast("Por favor, ingresa una dirección IP válida.")
                    }else {
                        showToast("Por favor, ingresa un puerto válido.")
                    }
                }
            } else {
                showToast("Por favor, ingresa una dirección y un puerto.")
            }
        }

        // Botón para enviar mensaje
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    sendMessage(message)
                }
            } else {
                showToast("El mensaje no puede estar vacío")
            }
        }

        // Botón para cerrar el puerto
        binding.closePortButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                closeConnection()
            }
        }

        // Botón para eliminar el socket
        binding.deleteSocketButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                deleteSocket()
            }
        }
    }

    //Fúncion que comprueba si la dirección IPV4 introducida es válida.
    fun esDireccionIPv4Valida(ip: String): Boolean {
        val partes = ip.split(".")
        if (partes.size != 4) return false
        for (parte in partes) {
            val numero = parte.toIntOrNull() ?: return false
            if (numero < 0 || numero > 255) return false
        }
        return true
    }

    //Fúncion que comprueba si la dirección IPV4 introducida es válida. Esta es con regex, preguntada a GPT para ver si obtenia una más facil.
    //La incluyo para saber que también es posible con otras librerias pero no la incluyo porque no la entiendo al 100% la validación.
//    fun esDireccionIPValida(ip: String): Boolean {
//        val patronIP = Regex("^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$")
//        return patronIP.matches(ip)
//    }

    private fun connectToServer() {
        try {
            // Si ya hay una conexión abierta, ciérrala primero
            socket?.close()
            socket = null
            output = null
            input = null

            // Ahora, establece la nueva conexión
            socket = Socket(serverIP, serverPort)
            output = PrintWriter(socket!!.getOutputStream(), true)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream()))

            runOnUiThread {
                binding.connectionStatus.text = "Status: Connected to $serverIP:$serverPort"
                binding.messageContainer.addView(createMessageTextView("Conexión establecida"))
                showToast("Conectado al servidor")
            }

            // Luego, comienza a escuchar mensajes
            CoroutineScope(Dispatchers.IO).launch {
                listenForMessages()
            }

        } catch (e: IOException) {
            socket?.close()
            socket = null
            runOnUiThread {
                binding.connectionStatus.text = "Status: Connection failed"
                showToast("Error al conectar: ${e.message}")
            }
        }
    }

    private fun listenForMessages() {
        try {
            var message: String?
            while (socket != null && !socket!!.isClosed && input != null) {
                message = input?.readLine()
                if (message != null) {
                    runOnUiThread {
                        binding.messageContainer.addView(createMessageToast(message, isMine = false))
                    }
                }
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error en la conexión: ${e.message}")
            }
        }
    }

    private fun closeConnection() {
        try {
            socket?.close()
            socket = null
            output = null
            input = null
            runOnUiThread {
                binding.connectionStatus.text = "Status: Disconnected"
                binding.messageContainer.addView(createMessageTextView("Conexión cerrada"))
                showToast("Conexión cerrada")
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al cerrar la conexión: ${e.message}")
            }
        }
    }

    private fun sendMessage(message: String) {
        try {
            output?.println(message)
            runOnUiThread {
                //binding.messageContainer.addView(createMessageTextView("Tú: $message"))
                binding.messageContainer.addView(createMessageToast("Tú: $message", isMine = true))
                binding.messageInput.text.clear()
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al enviar el mensaje: ${e.message}")
            }
        }
    }


    private fun createMessageToast(message: String, isMine: Boolean): TextView {
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        params.bottomMargin = 16

        if (isMine) {
            params.gravity = Gravity.END
            textView.setBackgroundResource(R.drawable.bocadillo_salida)
            params.leftMargin = 96
        } else {
            params.gravity = Gravity.START
            textView.setBackgroundResource(R.drawable.bocadillo_entrada)
            params.rightMargin = 96
        }

        textView.layoutParams = params
        textView.setPadding(16, 16, 16, 16)
        textView.text = message
        textView.setTextColor(Color.BLACK)
        textView.textSize = 16f

        return textView
    }

    private fun deleteSocket() {
        socket?.close()
        socket = null
        runOnUiThread {
            binding.connectionStatus.text = "Status: Disconnected"
            binding.messageContainer.removeAllViews()
            showToast("Socket eliminado")
        }
    }

    private fun createMessageTextView(message: String): TextView {
        val textView = TextView(this)
        textView.text = message
        textView.setPadding(8, 8, 8, 8)
        return textView
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}