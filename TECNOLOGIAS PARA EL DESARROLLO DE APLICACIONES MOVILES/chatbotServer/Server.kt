package es.ua.eps.chatbotserver

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList

val clients = CopyOnWriteArrayList<Socket>()
val ipsBloqueadas = arrayOf("")

fun main() {
    val serverPort = 6000
    val serverIp = InetAddress.getLocalHost().hostAddress
    println("Servidor escuchando en la IP $serverIp y puerto $serverPort...")

    val serverSocket = ServerSocket(serverPort)

    while (true) {
        try {
            val clientSocket: Socket = serverSocket.accept()
            val clientIp = clientSocket.inetAddress.hostAddress
            val clientPort = clientSocket.port
            println("Cliente conectado desde IP: $clientIp, Puerto: $clientPort")

            if (ipsBloqueadas.contains(clientIp)) {
                val output = PrintWriter(clientSocket.getOutputStream(), true)
                output.println("Conexión rechazada. Estás bloqueado en esta aplicación.")
                println("Cliente con IP $clientIp bloqueado y desconectado.")
                Thread.sleep(2000)
                clientSocket.close()
                continue
            } else {
                clients.add(clientSocket)
                Thread {
                    handleClient(clientSocket)
                }.start()
            }
        } catch (e: Exception) {
            println("Error al aceptar conexión o leer datos: ${e.message}")
        }
    }
}

fun handleClient(clientSocket: Socket) {
    try {
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val output = PrintWriter(clientSocket.getOutputStream(), true)
        val dataInputStream = clientSocket.getInputStream()
        var clientMessage: String?

        while (true) {
            val messageType = input.readLine() ?: break
            when {
                messageType.startsWith("TEXT:") -> {
                    clientMessage = messageType.removePrefix("TEXT:")
                    println("Mensaje recibido del cliente (${clientSocket.inetAddress.hostAddress}): $clientMessage")
                    broadcastMessage("TEXT:$clientMessage", clientSocket)
                }
                messageType.startsWith("IMAGE:") -> {
                    println("Recibiendo imagen del cliente (${clientSocket.inetAddress.hostAddress})...")
                    broadcastImage(dataInputStream, clientSocket)
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
    for (client in clients) {
        if (client != sender) {
            try {
                val output = PrintWriter(client.getOutputStream(), true)
                output.println(message)
            } catch (e: Exception) {
                println("Error enviando mensaje: ${e.message}")
            }
        }
    }
}

fun broadcastImage(inputStream: InputStream, sender: Socket) {
    val buffer = ByteArray(4096)
    var bytesRead: Int
    for (client in clients) {
        if (client != sender) {
            try {
                val outputStream = client.getOutputStream()
                outputStream.write("IMAGE:".toByteArray(Charsets.UTF_8))
                outputStream.flush()
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
                println("Imagen enviada a ${client.inetAddress.hostAddress}")
            } catch (e: Exception) {
                println("Error enviando imagen a ${client.inetAddress.hostAddress}: ${e.message}")
            }
        }
    }
}
