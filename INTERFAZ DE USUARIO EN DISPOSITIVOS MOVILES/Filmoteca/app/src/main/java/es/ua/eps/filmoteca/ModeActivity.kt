package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Surface {
                MyWindow()
            }
        }
    }

    @Composable
    fun MyWindow() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Para inicializar la aplicación en modo Layouts
            Button(
                onClick = { startFilmListActivity(Mode.Layouts) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = getString(R.string.lanzar_modo_app) + getString(R.string.con_layouts))
            }

            Button(
                onClick = { startFilmListActivityRecycler(Mode.Layouts) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = getString(R.string.lanzar_modo_app) + getString(R.string.con_layouts) + " Recycler")
            }

            //Para inicializar la aplicación en modo Compose
            Button(onClick = { startFilmListActivity(Mode.Compose) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = getString(R.string.lanzar_modo_app) + getString(R.string.con_compose))
            }

            Button(onClick = { startFilmListActivityRecycler(Mode.Compose) }) {
                Text(text = getString(R.string.lanzar_modo_app) + getString(R.string.con_compose) + " Recycler")
            }
        }
    }

    private fun startFilmListActivity(mode: Mode) {
        val intent = Intent(this, FilmListActivity::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
    }

    private fun startFilmListActivityRecycler(mode: Mode) {
        val intent = Intent(this, FilmListActivityRecycler::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
    }

}