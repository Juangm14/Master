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
import es.ua.eps.sqlite.data.User

@Composable
fun UpdateUser(navController: NavHostController, userId: Int){
    val context = LocalContext.current
    val userDatabase = UserDatabase(context)

    var user: User = userDatabase.getUserById(userId)!!

    var nombreComleto by remember { mutableStateOf(user.nombreCompleto) }
    var password by remember { mutableStateOf(user.password) }
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Email") }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nombreComleto,
                onValueChange = { nombreComleto = it },
                placeholder = { Text(text = "Nombre completo") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(text = "User name") }
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
                if (email.isEmpty() || nombreComleto.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    errorMessage = "Todos los campos son obligatorios"
                } else {
                    val result = userDatabase.updateUser(user.id, username, password, nombreComleto, email)
                    if (result != -1) {
                        errorMessage = ""
                        navController.popBackStack()
                    } else {
                        errorMessage = "Hubo un error al registrar el usuario"
                    }
                }
            }) {
                Text("Update user")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Back")
            }

        }
    }
}