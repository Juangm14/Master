package es.ua.eps.loginapp

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.ua.eps.loginapp.ui.theme.LoginAppTheme
import es.ua.eps.sqlite.UserData


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginAppTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("userData/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1
            UserData(navController = navController, userId = userId)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun handleLogin() {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Todos los campos son obligatorios"
        } else {
            val uri = Uri.parse("content://es.ua.eps.contentprovider/users")
            val projection = arrayOf("_id")
            val selection = "username=? AND password=?"
            val selectionArgs = arrayOf(username, password)

            Log.d("LoginScreen", "Consultando URI: content://es.ua.eps.contentprovider/users con selección: $selection")

            val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val userId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                cursor.close()
                errorMessage = ""
                navController.navigate("userData/${userId}")
                Toast.makeText(context, "Inicio de sesión exitoso: ID $userId", Toast.LENGTH_LONG).show()
            } else {
                errorMessage = "Usuario o contraseña incorrectos"
                cursor?.close()
            }
        }
    }

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
                Text("Login")
            }
        }
    }
}
