package es.ua.eps.sqliteroom

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUserRoom(navController: NavHostController) {
    val context = LocalContext.current
    val userDatabase = UserDatabaseRoom.getInstance(context).userDao()
    val usuarios = remember { mutableStateOf(emptyList<UserRoom>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            usuarios.value = userDatabase.loadAll()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Users") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
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
}