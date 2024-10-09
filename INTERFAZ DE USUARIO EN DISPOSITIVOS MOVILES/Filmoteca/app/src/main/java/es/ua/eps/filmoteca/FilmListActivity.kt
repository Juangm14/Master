package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

class FilmListActivity : AppCompatActivity() {

    private var mode: Mode = Mode.Layouts

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mode = intent.getSerializableExtra("MODE", Mode::class.java) ?: Mode.Layouts

        when(mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> setContent { initCompose() }
        }
    }

    private fun initLayouts(){
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.verPeliA.text = String.format(getString(R.string.ver_peli), "A")
        binding.verPeliB.text = String.format(getString(R.string.ver_peli), "B")

        binding.verPeliA.setOnClickListener {
            onClick(FilmDataActivity::class.java)
        }

        binding.verPeliB.setOnClickListener {
            onClick(FilmDataActivity::class.java)
        }

        binding.acercaDeBtn.setOnClickListener{
            onClick(AboutActivity::class.java)
        }
    }

    private fun onClick(nuevaClase: Class<*>) {
        val intent = Intent(this@FilmListActivity, nuevaClase)
        intent.putExtra("MODE", mode)
        startActivity(intent)
    }

    @Composable
    private fun initCompose() {
        Surface {
            MyWindow {
                AndroidView(
                factory = { ctx ->
                    Toolbar(ctx).apply {
                        setTitle(R.string.app_name)
                        setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                        setBackgroundColor(ContextCompat.getColor(ctx, R.color.customPurple))

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                )

                MyColumn {
                    Text(
                        text = getString(R.string.app_name) + " " + getString(R.string.con_compose),
                        modifier = Modifier.padding(16.dp)
                    )

                    MyButton({ onClick(FilmDataActivity::class.java) },
                        getString(R.string.ver_peli, "A"))

                    MyButton({ onClick(FilmDataActivity::class.java) },
                        getString(R.string.ver_peli, "B"))

                    MyButton({ onClick(AboutActivity::class.java) }, stringResource(id = R.string.acerca_de))
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }

    @Composable
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }

    @Composable
    fun MyButton(onClick: () -> Unit, text: String) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .padding(8.dp),
        ) {
            Text(text = text)
        }
    }

}