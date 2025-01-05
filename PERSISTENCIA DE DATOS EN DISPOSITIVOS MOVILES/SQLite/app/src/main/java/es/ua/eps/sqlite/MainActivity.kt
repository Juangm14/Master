package es.ua.eps.sqlite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.ua.eps.sqlite.data.User
import es.ua.eps.sqlite.ui.theme.SQLiteTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SQLiteTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginUser") {
        composable("userManagement") {
            UserManagement(navController)
        }
        composable("registerUser") {
            RegisterForm(navController)
        }
        composable(
            "updateUser/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = remember {
                it.arguments?.getInt("userId")
            }
            if (userId != null) {
                UpdateUser(navController, userId)
            }
        }
        composable("listUser") {
            ListUser(navController)
        }
        composable("loginUser") {
            LoginUser(navController)
        }
        composable(
            "userData/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = remember {
                it.arguments?.getInt("userId")
            }
            if (userId != null) {
                UserData(navController, userId)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagement(navController: NavHostController) {
    var selectedUserId by remember { mutableStateOf(-1) }
    var showDeletePopup by remember { mutableStateOf(false) }
    val database = UserDatabase(context = LocalContext.current)

    val usuarios = remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(Unit) {
        usuarios.value = database.getAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") }
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
                Button(onClick = {
                    navController.navigate("registerUser")
                }) {
                    Text("New user")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                DropDownUsers(usuarios.value, selectedUserId) { userId ->
                    selectedUserId = userId
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    if (selectedUserId != -1) {
                        navController.navigate("updateUser/$selectedUserId")
                    }
                }) {
                    Text("Update user")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    if (selectedUserId != -1) {
                        showDeletePopup = true
                    }
                }) {
                    Text("Delete user")
                }

                if (showDeletePopup) {
                    DeleteUserPopup(
                        onDismiss = { showDeletePopup = false },
                        onConfirm = {
                            if (selectedUserId != -1) {

                                database.deleteUser(selectedUserId)

                                usuarios.value = database.getAllUsers()

                                if (!usuarios.value.any { it.id == selectedUserId }) {
                                    selectedUserId = -1
                                }

                                showDeletePopup = false
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    navController.navigate("listUser")
                }) {
                    Text("List users")
                }
            }
        }
    }
}

@Composable
fun DropDownUsers(
    usuarios: List<User>,  // Aceptamos la lista de usuarios como parámetro
    selectedUserId: Int,
    onUserSelected: (Int) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val selectedUser = remember { mutableStateOf<User?>(null) }

    if (usuarios.isNotEmpty() && (selectedUserId == -1 || selectedUser == null)) {
        selectedUser.value = usuarios[0]
        onUserSelected(selectedUser.value!!.id)
    }

    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }
        ) {
            Text(
                text = selectedUser.value?.username ?: "No users",
                modifier = Modifier.padding(8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "DropDown Icon"
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }
        ) {
            if (usuarios.isNotEmpty()) {
                usuarios.forEach { user ->
                    DropdownMenuItem(
                        text = { Text(text = user.username) },
                        onClick = {
                            isDropDownExpanded.value = false
                            selectedUser.value = user
                            onUserSelected(user.id) // Devuelve el ID del usuario seleccionado
                        }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text(text = "No users available") },
                    onClick = { isDropDownExpanded.value = false }
                )
            }
        }
    }
}

@Composable
fun DeleteUserPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Delete user")
        },
        text = {
            Text(text = "Do you really want to delete the selected user?")
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("CANCEL")
            }
        }
    )
}

fun createBackup(context: Context) {
    val dbPath = context.getDatabasePath("user_database.db").absolutePath
    val backupDir = File(context.getExternalFilesDir(null), "SQLiteBackups")
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
    val dbPath = context.getDatabasePath("user_database.db").absolutePath
    val backupFile =
        File(context.getExternalFilesDir(null), "SQLiteBackups/user_database_backup.db")

    if (!backupFile.exists()) {
        Toast.makeText(context, "El archivo de backup no existe", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        FileInputStream(backupFile).use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }
        Toast.makeText(context, "Backup restaurado exitosamente", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Error al restaurar el backup", Toast.LENGTH_SHORT).show()
    }
}
