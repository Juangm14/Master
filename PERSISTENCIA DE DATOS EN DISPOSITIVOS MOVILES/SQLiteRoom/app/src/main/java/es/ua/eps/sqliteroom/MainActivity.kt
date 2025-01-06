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
import androidx.compose.material3.AlertDialog
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
import es.ua.eps.sqliteroom.ListUserRoom
import es.ua.eps.sqliteroom.LoginUserRoom
import es.ua.eps.sqliteroom.RegisterFormRoom
import es.ua.eps.sqliteroom.UpdateUserRoom
import es.ua.eps.sqliteroom.UserDataRoom
import es.ua.eps.sqliteroom.UserDatabaseRoom
import es.ua.eps.sqliteroom.UserRoom
import es.ua.eps.sqliteroom.ui.theme.SqliteRoomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SqliteRoomTheme {
                val navController = rememberNavController()
                NavigationGraphRoom(navController)
            }
        }
    }
}

@Composable
fun NavigationGraphRoom(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "loginUserRoom") {
        composable("loginUserRoom") {
            LoginUserRoom(navController)
        }

        composable("userManagementRoom") {
            UserManagementRoom(navController)
        }

        composable("registerUserRoom") {
            RegisterFormRoom(navController)
        }
        composable(
            "updateUserRoom/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = remember {
                it.arguments?.getInt("userId")
            }
            if (userId != null) {
                UpdateUserRoom(navController, userId)
            }
        }

        composable("listUserRoom") {
            ListUserRoom(navController)
        }

        composable(
            "userDataRoom/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = remember {
                it.arguments?.getInt("userId")
            }
            if (userId != null) {
                UserDataRoom(navController, userId)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserManagementRoom(navController: NavHostController) {
    var selectedUserId by remember { mutableStateOf(-1) }
    var showDeletePopup by remember { mutableStateOf(false) }
    val database = UserDatabaseRoom.getInstance(LocalContext.current)

    val usuarios = remember { mutableStateOf(emptyList<UserRoom>()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            usuarios.value = database.userDao().loadAll()
        }
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
                    navController.navigate("registerUserRoom")
                }) {
                    Text("New user")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                DropDownUsersRoom(usuarios.value, selectedUserId) { userId ->
                    selectedUserId = userId
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    if (selectedUserId != -1) {
                        navController.navigate("updateUserRoom/$selectedUserId")
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
                    DeleteUserPopupRoom(
                        onDismiss = { showDeletePopup = false },
                        onConfirm = {
                            if (selectedUserId != -1) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.userDao().deleteById(selectedUserId)

                                    withContext(Dispatchers.Main) {
                                        usuarios.value = withContext(Dispatchers.IO) {
                                            database.userDao().loadAll()
                                        }

                                        if (usuarios.value.none { it.uid == selectedUserId }) {
                                            selectedUserId = -1
                                        }

                                        showDeletePopup = false
                                    }
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = {
                    navController.navigate("listUserRoom")
                }) {
                    Text("List users")
                }
            }
        }
    }
}

@Composable
fun DropDownUsersRoom(
    usuarios: List<UserRoom>,
    selectedUserId: Int,
    onUserSelected: (Int) -> Unit
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val selectedUser = remember { mutableStateOf<UserRoom?>(null) }

    if (usuarios.isNotEmpty() && (selectedUserId == -1 || selectedUser == null)) {
        selectedUser.value = usuarios[0]
        onUserSelected(selectedUser.value!!.uid)
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
                text = selectedUser.value?.name ?: "No users",
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
                        text = { Text(text = user.name) },
                        onClick = {
                            isDropDownExpanded.value = false
                            selectedUser.value = user
                            onUserSelected(user.uid)
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
fun DeleteUserPopupRoom(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
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

