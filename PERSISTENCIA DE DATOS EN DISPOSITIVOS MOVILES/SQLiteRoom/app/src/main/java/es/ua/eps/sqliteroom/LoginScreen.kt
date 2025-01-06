package es.ua.eps.sqliteroom

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import es.ua.eps.sqlite.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlin.system.exitProcess

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUserRoom(navController: NavHostController) {
    val context = LocalContext.current
    val database = UserDatabaseRoom.getInstance(LocalContext.current)

    var menuExpanded by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun handleLogin() {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Todos los campos son obligatorios"
        } else {
            //Hilo de fondo.
            CoroutineScope(Dispatchers.IO).launch {
                val userId = database.userDao().login(username, password)

                //Main actualiza los datos tras obtener el resultado del hilo de fondo.
                withContext(Dispatchers.Main) {
                    if (userId != null) {
                        errorMessage = ""
                        navController.navigate("userDataRoom/$userId")
                    } else {
                        errorMessage = "El usuario no existe."
                    }
                }
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
                                    navController.navigate("userManagementRoom")
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

fun createBackup(context: Context) {
    val dbPath = context.getDatabasePath("user_database").absolutePath
    val backupDir = File(context.getExternalFilesDir(null), "")

    if (!backupDir.exists() && !backupDir.mkdirs()) {
        Toast.makeText(context, "No se pudo crear el directorio de backup", Toast.LENGTH_SHORT)
            .show()
        return
    }
    val backupFile = File(backupDir, "user_database_backup.db")

    try {
        if (!File(dbPath).exists()) {
            Toast.makeText(context, "La base de datos no existe", Toast.LENGTH_SHORT).show()
            return
        }

        FileInputStream(dbPath).use { input ->
            FileOutputStream(backupFile).use { output ->
                input.copyTo(output)
            }
        }
        Toast.makeText(
            context,
            "Backup creado exitosamente: ${backupFile.absolutePath}",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Error al crear el backup", Toast.LENGTH_SHORT).show()
    }
}

fun restoreBackup(context: Context) {
    val dbPath = context.getDatabasePath("user_database").absolutePath
    val backupFile = File(context.getExternalFilesDir(null), "user_database_backup.db")

    if (!backupFile.exists()) {
        Toast.makeText(context, "El archivo de backup no existe", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val db = UserDatabaseRoom.getInstance(context)
        db.close()

        FileInputStream(backupFile).use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }

        UserDatabaseRoom.getInstance(context)

        Toast.makeText(context, "Backup restaurado exitosamente", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Error al restaurar el backup", Toast.LENGTH_SHORT).show()
    }
}
