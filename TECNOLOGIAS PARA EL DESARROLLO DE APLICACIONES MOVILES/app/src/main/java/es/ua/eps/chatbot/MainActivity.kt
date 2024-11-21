package es.ua.eps.chatbot

import android.graphics.Color
import android.graphics.Typeface
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
import java.text.SimpleDateFormat
import java.util.*



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

        // Deshabilitar el botón de envío y el campo de texto
        binding.sendButton.isEnabled = false
        binding.messageInput.isEnabled = false

        binding.newSocketButton.setOnClickListener {
            val targetAddress = binding.targetAddress.text.toString()
            aliasClient = binding.alias.text.toString()

            val port = binding.port.text.toString()

            if (targetAddress.isNotBlank() && port.isNotBlank()) {
                serverIP = targetAddress
                serverPort = port.toIntOrNull() ?: 0
                if (serverPort > 0 && esDireccionIPv4Valida(targetAddress)) {

                    if (aliasClient.isEmpty()) {
                        showToast("Por favor, ingresa un alias válido.")
                    }else {
                        CoroutineScope(Dispatchers.IO).launch {
                            closeConnection()
                            connectToServer()
                        }
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

    // Encripta un texto usando AES 16 Bytes y lo convertimos a BA64.
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

    // Decodifica el BA64 primero y luego desencripta un texto cifrado en base64 usando AES 16 Bytes
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

    //En caso de tener un socket activo, lo cierra y crea otro nuevo. Se muestra un mensaje de que ha establecido conexión o error en caso contrario.
    private fun connectToServer() {
        try {
            socket?.close()
            socket = Socket(serverIP, serverPort)
            output = PrintWriter(socket!!.getOutputStream(), true)
            input = BufferedReader(InputStreamReader(socket!!.getInputStream()))

            runOnUiThread {
                binding.connectionStatus.text = "Conectado a $serverIP:$serverPort"
                showToast("Conexión establecida")

                // Habilitar el botón de envío y el campo de texto
                binding.sendButton.isEnabled = true
                binding.messageInput.isEnabled = true
            }

            CoroutineScope(Dispatchers.IO).launch {
                listenForMessages()
            }
        } catch (e: IOException) {
            runOnUiThread {
                binding.connectionStatus.text = "Conexión fallida"
                showToast("Error al conectar: ${e.message}")
                // Deshabilitar el botón de envío y el campo de texto
                binding.sendButton.isEnabled = false
                binding.messageInput.isEnabled = false
            }
        }
    }

    // Escucha los mensajes entrantes del servidor y llama a las funciones necesarias para descifrar (con la misma clave secreta) y
    // escribir los mensajes extrayendo el alias mediante un split de ":"
    private fun listenForMessages() {
        try {
            var message: String?
            while (input?.readLine().also { message = it } != null) {
                runOnUiThread {
                    val mensajeDescifrado = descifrarTexto(message!!, claveSecreta = "clavesecreta1234")
                    val partes = mensajeDescifrado.split(":", limit = 2)
                    if (partes.size == 2) {
                        val alias = partes[0] // Extraer alias
                        val contenido = partes[1] // Extraer mensaje
                        binding.messageContainer.addView(createMessageLayout(contenido, isMine = false, alias))
                    } else {
                        binding.messageContainer.addView(createMessageLayout(mensajeDescifrado, isMine = false, "Desconocido"))
                    }
                }
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error en la conexión: ${e.message}")
            }
        }
    }

    //Se envia un mensaje al servidor. Con el alias del cliente y su mensaje cifrado mediante AESS 16bytes (Los Bytes lo define la clave secreta)
    fun sendTextMessage(message: String) {
        val mensajeCifrado = cifrarTexto("$aliasClient:$message", "clavesecreta1234") //Clave secreta
        try {
            output?.println(mensajeCifrado)  // Enviar el mensaje cifrado
            output?.flush()
            runOnUiThread {
                binding.messageContainer.addView(createMessageLayout("$message", isMine = true, aliasClient))
                binding.messageInput.text.clear()
            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al enviar el mensaje: ${e.message}")
            }
        }
    }

    //Cerramos la conexión con el servidor(cierra el socket)
    private fun closeConnection() {
        try {
            socket?.close()
            socket = null
            runOnUiThread {
                binding.connectionStatus.text = "Desconectado"

                // Deshabilitar el botón de envío y el campo de texto
                binding.sendButton.isEnabled = false
                binding.messageInput.isEnabled = false

            }
        } catch (e: IOException) {
            runOnUiThread {
                showToast("Error al cerrar la conexión: ${e.message}")
            }
        }
    }

    //Obtenemos la hora central española.
    fun obtenerHora(): String {
        val zonaEspaña = TimeZone.getTimeZone("Europe/Madrid")
        val calendario = Calendar.getInstance(zonaEspaña)
        val formato = SimpleDateFormat("HH:mm", Locale.getDefault()) // Cambia el formato si es necesario
        formato.timeZone = zonaEspaña
        return formato.format(calendario.time)
    }

    // Crea un layout para mostrar el mensaje con su alias, contenido y hora. Empleamos drawables para los colores y la forma del mensaje y sea una forma de bocadillo.
    private fun createMessageLayout(message: String, isMine: Boolean, alias: String): LinearLayout {

        val messageLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = if (isMine) Gravity.END else Gravity.START
            params.topMargin = 16
            params.bottomMargin = 16
            if (isMine) {
                params.leftMargin = 150
            } else {
                params.rightMargin = 150
            }
            layoutParams = params
            setPadding(16, 16, 16, 16)
            setBackgroundResource(if (isMine) R.drawable.bocadillo_salida else R.drawable.bocadillo_entrada)
        }

        val aliasText = TextView(this).apply {
            text = alias
            setTextColor(Color.BLACK)
            textSize = 14f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            gravity = Gravity.START
        }
        messageLayout.addView(aliasText)

        val messageText = TextView(this).apply {
            text = message
            setTextColor(Color.BLACK)
            textSize = 14f
        }

        val timestamp = TextView(this).apply {
            text = obtenerHora() // Cambiar por hora real si está disponible
            setTextColor(Color.GRAY)
            textSize = 10f
            gravity = Gravity.END
        }

        messageLayout.addView(messageText)
        messageLayout.addView(timestamp)

        return messageLayout
    }

    // Muestra mensaje mediante Toast
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Valida si una dirección IP es válida en formato IPv4
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
