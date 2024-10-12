package es.ua.eps.filmoteca

import FilmArrayAdapterRecycler
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.filmoteca.databinding.ActivityFilmListRecyclerBinding

class FilmListActivityRecycler : AppCompatActivity() {

    private var mode: Mode = Mode.Layouts
    private lateinit var binding: ActivityFilmListRecyclerBinding
    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

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
        binding = ActivityFilmListRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.listFilms
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        val peliculas = FilmDataSource.films
        val adapter = FilmArrayAdapterRecycler(peliculas)
        recyclerView?.adapter = adapter
        this.adapter = adapter

        adapter.setOnItemClickListener { film ->
            Toast.makeText(this, "Pulsado: ${film.title}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FilmDataActivity::class.java).apply {
                putExtra("MODE", mode)
                putExtra("FILM_INDEX", peliculas.indexOf(film))
            }
            startActivity(intent)
        }
    }

    private fun onClick(nuevaClase: Class<*>) {
        val intent = Intent(this@FilmListActivityRecycler, nuevaClase)
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