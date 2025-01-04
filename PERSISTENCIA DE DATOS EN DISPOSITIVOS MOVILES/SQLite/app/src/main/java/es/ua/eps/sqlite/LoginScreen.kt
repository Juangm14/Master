package es.ua.eps.sqlite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlin.system.exitProcess

@Composable
fun LoginUser(navController: NavHostController) {
    val context = LocalContext.current
    val userDatabase = UserDatabase(context)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(text = "User name") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // Mostrar mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }

            Button(onClick = {
                if (password.isEmpty() || username.isEmpty()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else {
                    val userId = userDatabase.login(username, password)
                    if (userId != -1) {
                        errorMessage = ""
                        navController.navigate("userData/${userId}")
                    } else {
                        errorMessage = "El usuario no existe."
                    }
                }
            }) {
                Text("Login")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = {
                exitProcess(0)
            }) {
                Text("Close")
            }
        }
    }
}