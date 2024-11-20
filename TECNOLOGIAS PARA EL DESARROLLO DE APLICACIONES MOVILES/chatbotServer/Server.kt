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
        val clientIp = clientSocket.inetAddress.hostAddress
        var clientMessage: String?

        while (true) {
            clientMessage = input.readLine() ?: break  // Leemos el mensaje

            // Verificamos que el mensaje no sea vacío o nulo
            if (!clientMessage.isNullOrEmpty()) {
                println("Mensaje recibido de $clientIp: $clientMessage")
                broadcastMessage(clientMessage, clientSocket)  // Enviamos el mensaje cifrado
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