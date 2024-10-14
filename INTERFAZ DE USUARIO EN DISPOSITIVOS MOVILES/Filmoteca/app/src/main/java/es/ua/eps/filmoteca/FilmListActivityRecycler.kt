package es.ua.eps.filmoteca

import FilmArrayAdapterRecycler
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.anyadir_peli -> {
                return true
            }
            R.id.acerca_de -> {
                onClick(AboutActivity::class.java)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
    fun initCompose() {
        val films = FilmDataSource.films

        val contexto = LocalContext.current

        Surface {
            MyWindow {
                AndroidView(
                    factory = { content ->
                        Toolbar(content).apply {
                            setTitle(R.string.app_name)
                            setTitleTextColor(
                                ContextCompat.getColor(
                                    content,
                                    android.R.color.white
                                )
                            )
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    content,
                                    R.color.customPurple
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ListaFilms(films) { pelicula ->

                    val intent = Intent(contexto, FilmDataActivity::class.java).apply {
                        putExtra("MODE", mode)
                        putExtra("FILM_INDEX", films.indexOf(pelicula))
                        Toast.makeText(contexto, "Pulsado: ${pelicula.title}", Toast.LENGTH_SHORT).show()
                    }
                    contexto.startActivity(intent)
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            content()
        }
    }

    @Composable
    fun ListaFilms(films: List<Film>, onItemClick: (Film) -> Unit) {
        LazyColumn {
            items(films) { film ->
                ItemFilm(film) { clickedFilm ->
                    onItemClick(clickedFilm)
                }
            }
        }
    }

    @Composable
    fun ItemFilm(film: Film, onClick: (Film) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(film) }
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = film.imageResId),
                contentDescription = "",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = film.title ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = film.director ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}