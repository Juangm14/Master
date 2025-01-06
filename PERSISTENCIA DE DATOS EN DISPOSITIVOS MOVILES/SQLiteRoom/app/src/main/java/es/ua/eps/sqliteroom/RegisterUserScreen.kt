package es.ua.eps.sqliteroom

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormRoom(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDatabase = UserDatabaseRoom.getInstance(context).userDao()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New User") }
            )
        }
    ) {
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
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "Email") }
                )

                Spacer(modifier = Modifier.padding(8.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Login") }
                )

                Spacer(modifier = Modifier.padding(8.dp))

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

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = androidx.compose.ui.graphics.Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    if (email.isEmpty() || name.isEmpty() || password.isEmpty() || username.isEmpty()) {
                        errorMessage = "Todos los campos son obligatorios"
                    } else {
                        val user = UserRoom(username = username, password = password, name = name, email = email)
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                userDatabase.insert(user)
                            }
                            navController.popBackStack()
                        }

                    }
                }) {
                    Text("New user")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    navController.popBackStack() // Volver a la pantalla anterior
                }) {
                    Text("Back")
                }

            }
        }
    }
}
