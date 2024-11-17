package es.ua.eps.chatbotserver

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList

val clients = CopyOnWriteArrayList<Socket>()
val ipsBloqueadas = arrayOf("") //arrayOf("192.168.100.3") //Para cuando quiera bloquear mi teléfono.

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

            // Verifica si la IP está bloqueada
            if (ipsBloqueadas.contains(clientIp)) {
                val output = PrintWriter(clientSocket.getOutputStream(), true)
                // Envía el mensaje de rechazo
                output.println("Conexión rechazada. Estás bloqueado en esta aplicación.")
                println("Cliente con IP $clientIp bloqueado y desconectado.")
                
                // Cierra la conexión después de enviar el mensaje. Esto está fatal porque al cortar la conexion con el cliente desde el servidor aunque le llegue algun mensaje, no le da tiempo a mostrarlo porque se corta la conexión muy rápido.
                // Lo que he hecho es cortar la conexión desde el cliente sin necesidad de tener que darle a close socket y poner un temporizador de 2 segundos para que le de tiempo a mostrar el mensaje enconces ya cierro el socket del server.
                Thread.sleep(2000)
                clientSocket.close()
                continue
            } else {
                // Maneja la conexión del cliente en un hilo separado
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

        var clientMessage: String?

        while (input.readLine().also { clientMessage = it } != null) {
            println("Mensaje recibido del cliente (${clientSocket.inetAddress.hostAddress}): $clientMessage")
            broadcastMessage(clientMessage!!, clientSocket)
        }

    } catch (e: Exception) {
        println("Error al manejar cliente: ${e.message}")
    } finally {
        // Eliminar al cliente al desconectarse
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
                output.println("Mensaje de ${sender.inetAddress.hostAddress}: $message")
            } catch (e: Exception) {
                println("Error enviando mensaje: ${e.message}")
            }
        }
    }
}
