package es.ua.eps.sqlite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.ua.eps.sqlite.data.User
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun ListUser(navController: NavHostController) {
    val context = LocalContext.current
    val database = UserDatabase(context)
    val usuarios = remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(Unit) {
        usuarios.value = database.getAllUsers()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("List Users", modifier = Modifier.fillMaxWidth(), style = androidx.compose.material3.MaterialTheme.typography.titleLarge)

        // Encabezados de la tabla
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("User", modifier = Modifier.weight(1f))
            Text("E-Mail", modifier = Modifier.weight(1f))
        }

        // Línea divisoria
        androidx.compose.foundation.Canvas(
            modifier = Modifier.fillMaxWidth().height(1.dp)
        ) {
            drawLine(color = androidx.compose.ui.graphics.Color.Gray, start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(size.width, 0f))
        }

        LazyColumn {
            items(usuarios.value) { user ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(user.username, modifier = Modifier.weight(1f))
                    Text(user.email, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("BACK")
        }
    }
}
