package es.ua.eps.chatbotserver

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


// Lista concurrente para almacenar los sockets de los clientes conectados
val clients = CopyOnWriteArrayList<Socket>()

// Lista de direcciones IP bloqueadas (actualmente solo contiene una cadena vacía). Si añadimos la Ip de un dispositivo e intentas conectarte falla.
val ipsBloqueadas = arrayOf("")

//Misma función que tiene el cliente para descifrar la ip del receptor.
fun descifrarTexto(textoCifrado: String, claveSecreta: String): String {
    try {
        // Convierte la clave secreta en un formato adecuado
        val secretKey = SecretKeySpec(claveSecreta.toByteArray(Charsets.UTF_8), "AES")

        // Inicializa el cifrador
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        // Decodifica el texto cifrado de base64
        val textoCifradoBytes = Base64.getDecoder().decode(textoCifrado)

        // Descifra el texto
        val textoDescifrado = cipher.doFinal(textoCifradoBytes)

        // Convierte el texto descifrado de bytes a String
        return String(textoDescifrado, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun main() {
    val serverPort = 6000
    val serverIp = InetAddress.getLocalHost().hostAddress  // Obtiene la IP local del servidor
    println("Servidor escuchando en la IP $serverIp y puerto $serverPort...")

    val serverSocket = ServerSocket(serverPort) // Crea un socket de servidor en el puerto especificado


    while (true) {
        try {
            // Acepta la conexión de un cliente
            val clientSocket: Socket = serverSocket.accept() 
            val clientIp = clientSocket.inetAddress.hostAddress 
            val clientPort = clientSocket.port
            println("Cliente conectado desde IP: $clientIp, Puerto: $clientPort")

            // Si la IP del cliente está bloqueada, se rechaza la conexión y se envia un mensaje de rechazo al cliente. Se le dan 2 segundos para el cierre del socket
            //para que se muestre el mensaje en el cliente.
            if (ipsBloqueadas.contains(clientIp)) {
                val output = PrintWriter(clientSocket.getOutputStream(), true) 
                output.println("Conexión rechazada. Estás bloqueado en esta aplicación.") 
                println("Cliente con IP $clientIp bloqueado y desconectado.") 
                Thread.sleep(2000) 
                clientSocket.close() 
                continue // Continúa esperando otro cliente
            } else {
                clients.add(clientSocket) // Si el cliente no está bloqueado, se añade a la lista de clientes y se crea otro hilo para mantener la comunicación.
                Thread {
                    handleClient(clientSocket)
                }.start()
            }
        } catch (e: Exception) {
            // En caso de error al aceptar conexión o leer datos, se muestra el mensaje de error
            println("Error al aceptar conexión o leer datos: ${e.message}")
        }
    }
}

fun handleClient(clientSocket: Socket) {
    try {
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream())) // Lee los datos del cliente
        val output = PrintWriter(clientSocket.getOutputStream(), true)
        val clientIp = clientSocket.inetAddress.hostAddress //La obtenemos para ver las Ips en el servidor.
        var clientMessage: String?

        while (true) {
            clientMessage = input.readLine() ?: break  // Leemos el mensaje

            // Verificamos que el mensaje no sea vacío o nulo y se envía
            if (!clientMessage.isNullOrEmpty()) {
                println("Mensaje recibido de $clientIp: $clientMessage")
                //En caso de que sea mensaje privado tendrá ":" sin cifrar y obtendremos la ip del receptor privada y el mensaje cifrado(aún faltaria descifrar otra vez).
                val partes = clientMessage.split(":")
                if (partes.size == 2) {
                    //Desciframos la ip del receptor y procedemos a enviar el mensaje cifrado.
                    val recipientIp = descifrarTexto(partes[0], "claveservidor123")
                    val message = partes[1]

                    sendMessageToOne(message, recipientIp, clientSocket) // Enviamos el mensaje cifrado solo a un cliente.
                } else {
                    broadcastMessage(clientMessage, clientSocket) // Enviamos el mensaje cifrado a todos los clientes conectados.
                }
            }
        }
    } catch (e: Exception) {
        println("Error al manejar cliente: ${e.message}")
    } finally {
        println("Cliente desconectado: ${clientSocket.inetAddress.hostAddress}")
        clients.remove(clientSocket)
        try {
            clientSocket.close()
        } catch (e: Exception) {
            println("Error cerrando socket: ${e.message}")
        }
    }
}



fun broadcastMessage(message: String, sender: Socket) {
    val senderIp = sender.inetAddress.hostAddress  // IP del cliente que envió el mensaje
    
    //Recorremos todos los clientes y enviamos el mensaje a todos, siempre y cuando sea distinto al que lo ha enviado.
    for (client in clients) {
        if (client != sender) {
            try {
                val output = PrintWriter(client.getOutputStream(), true)
                output.println("$message")  // Solo enviamos el mensaje
            } catch (e: Exception) {
                println("Error enviando mensaje: ${e.message}")
            }
        }
    }
}

//Mandamos solo el mensaje a un cliente en concreto, distinto al que manda el mensaje.
fun sendMessageToOne(message: String, recipientIp: String, sender: Socket) {
    for (client in clients) {
        if (client.inetAddress.hostAddress == recipientIp && client != sender) {
            try {
                val output = PrintWriter(client.getOutputStream(), true)
                output.println("$message")
                println("Mensaje enviado a $recipientIp")
                return
            } catch (e: Exception) {
                println("Error enviando mensaje a $recipientIp: ${e.message}")
            }
        }
    }
    println("No se encontró un cliente con la IP $recipientIp")
}

