package es.ua.eps.sqliteroom

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataRoom(navController: NavController, userId: Int) {

    val context = LocalContext.current
    val userDatabase = UserDatabaseRoom.getInstance(context).userDao()
    var user by remember { mutableStateOf(UserRoom(0, "", "", "", "")) }

    LaunchedEffect(userId) {
        val loadedUser = withContext(Dispatchers.IO) {
            userDatabase.loadUserById(userId)
        }

        user = loadedUser
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Data") }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (user.uid != 0) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Welcome   ",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Text(user.name)
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "User name   ",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Text(user.username)
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(onClick = {
                        navController.popBackStack() // Volver a la pantalla anterior
                    }) {
                        Text("Back")
                    }
                }
            }else{
                Text(text = "Usuario no encontrado", color = androidx.compose.ui.graphics.Color.Red)
            }
        }
    }
}