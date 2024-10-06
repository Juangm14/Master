package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityFilmEditBinding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmEditBinding

    private var mode: Mode = Mode.Layouts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mode = intent.getSerializableExtra("MODE", Mode::class.java) ?: Mode.Layouts

        when (mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> setContent { initCompose() }
        }
    }

    private fun initLayouts() {
        binding = ActivityFilmEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Ponemos setResult para devolver el código de resultado a la actividad padre.
        binding.guardarEditBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.cancelarEditBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    // MEDIANTE JETPACK COMPOSE
    @Composable
    private fun initCompose() {
        Surface {
            MyWindow {
                MyColumn {
                    Text(
                        text = getString(R.string.edit_peli_btn) + " " + getString(R.string.con_compose),
                        modifier = Modifier.padding(8.dp)
                    )
                    MyButton({ onClickGuardar() }, stringResource(id = R.string.guardar_btn))
                    MyButton({ onClickCancelar() }, stringResource(id = R.string.cancelar_btn))
                }
            }
        }
    }

    // Composable que define el layout general de la pantalla.
    @Composable
    fun MyWindow(content: @Composable ColumnScope.() -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }

    // Composable que define la columna centrada.
    @Composable
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            content()
        }
    }

    // Composable para el botón.
    @Composable
    fun MyButton(onClick: () -> Unit, text: String) {
        Button(
            onClick = { onClick() },
            modifier = Modifier.width(210.dp).padding(bottom = 8.dp),
        ) {
            Text(text = text)
        }
    }

    // Acción al guardar.
    private fun onClickGuardar() {
        setResult(RESULT_OK)
        finish()
    }

    // Acción al cancelar.
    private fun onClickCancelar() {
        setResult(RESULT_CANCELED)
        finish()
    }


}