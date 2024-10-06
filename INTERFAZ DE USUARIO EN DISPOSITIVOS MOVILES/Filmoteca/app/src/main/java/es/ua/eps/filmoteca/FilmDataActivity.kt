package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import android.content.Context
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding

class FilmDataActivity : AppCompatActivity() {

    companion object {
        val EXTRA_FILM_TITLE = "EXTRA_FILM_TITLE"
    }

    private var mode: Mode = Mode.Layouts

    private lateinit var binding: ActivityFilmDataBinding

    private val editFilmResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //Se han guardado los cambios en la edición de la película correctamente.
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, getString(R.string.film_edited_success_msg), Toast.LENGTH_SHORT).show()
            //Se ha cancelado la edición de la película.
        } else if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.film_edit_cancel_msg), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Si es nulo ponemos por defecto Layouts
        mode = intent.getSerializableExtra("MODE", Mode::class.java) ?: Mode.Layouts
        initUI()
    }

    private fun initUI() {
        when (mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> setContent { initCompose() }
        }
    }

    private fun initLayouts() {
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.peliDatosLabel.setText(getString(R.string.datos_peli_label) + " " + EXTRA_FILM_TITLE)

        binding.verPeliRelacBtn.setOnClickListener {
            onClick(FilmDataActivity::class.java)
        }

        binding.editPeliBtn.setOnClickListener {
            onClickEdit();
        }

        binding.backMainBtn.setOnClickListener {
            onClick(FilmListActivity::class.java, flag = Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    //Hacemos que el parámetro flag sea opcional para que no sea necesario pasarlo cuando no queramos emplearlo.
    fun onClick(nuevaClase: Class<*>, flag: Int? = null) {
        val intent = Intent(this@FilmDataActivity, nuevaClase)
        intent.putExtra("MODE", mode)

        if (flag != null){
            intent.flags = flag
        }

        startActivity(intent)
    }

    // MEDIANTE JETPACK COMPOSE
    @Composable
    private fun initCompose() {
        Surface {
            MyWindow {
                MyColumn {
                    Text(
                        text = getString(R.string.datos_peli_label) + " " + EXTRA_FILM_TITLE + " " + getString(R.string.con_compose),
                        modifier = Modifier.padding(8.dp)
                    )

                    MyButton(onClick = { onClickEdit() }, text = stringResource(id = R.string.edit_peli_btn))

                    MyButton(onClick = { onClick(FilmDataActivity::class.java) }, text = stringResource(id = R.string.relac_peli_btn))

                    MyButton(onClick = { onClick(FilmListActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP) }, text = stringResource(id = R.string.volver_peli))
                }
            }
        }
    }

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

    @Composable
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            content()
        }
    }

    @Composable
    fun MyButton(onClick: () -> Unit, text: String) {
        Button(
            onClick = { onClick() },
            modifier = Modifier.width(210.dp).padding(bottom = 8.dp),
        ) {
            Text(text = text)
        }
    }

    private fun onClickEdit() {
        val intent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
        intent.putExtra("MODE", mode)
        editFilmResultLauncher.launch(intent)
    }

    private fun onClickBack() {
        finish()
    }
}