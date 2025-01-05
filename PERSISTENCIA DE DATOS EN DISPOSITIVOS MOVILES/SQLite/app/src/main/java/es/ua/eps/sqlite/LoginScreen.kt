package es.ua.eps.sqlite

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlin.system.exitProcess

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUser(navController: NavHostController) {
    val context = LocalContext.current
    val userDatabase = UserDatabase(context)

    var menuExpanded by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun handleLogin() {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Todos los campos son obligatorios"
        } else {
            val userId = userDatabase.login(username, password)
            if (userId != -1) {
                errorMessage = ""
                navController.navigate("userData/$userId")
            } else {
                errorMessage = "El usuario no existe."
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SQLite App") },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = "Menú"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Crear copia de seguridad") },
                                onClick = {
                                    menuExpanded = false
                                    createBackup(context)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Restaurar copia de seguridad") },
                                onClick = {
                                    menuExpanded = false
                                    restoreBackup(context)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Gestionar usuarios") },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate("userManagement")
                                }
                            )
                        }
                    }
                }
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
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Nombre de usuario") }
                )

                Spacer(modifier = Modifier.padding(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Contraseña") }
                )

                Spacer(modifier = Modifier.padding(8.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = androidx.compose.ui.graphics.Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                }

                Button(onClick = { handleLogin() }) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = { exitProcess(0) }) {
                    Text("Cerrar")
                }
            }
        }
    }
}
