package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.ListView
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat

class FilmListActivity : AppCompatActivity() {

    private var mode: Mode = Mode.Layouts

    private lateinit var binding: ActivityFilmListBinding

    private lateinit var listaPelis: MutableList<Film>
    private lateinit var adaptador: FilmArrayAdapter
    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.anyadir_peli -> {
                anyadirPeli()
                return true
            }
            R.id.acerca_de -> {
                onClick(AboutActivity::class.java, flag = Intent.FLAG_ACTIVITY_CLEAR_TOP)
                return true
            }
            // NavigateUpTo equivale a haber lanzado el intent con el FLAG_ACTIVITY_CLEAR_TOP, pero aqui hay que hacerlo así porque sino escala en jerarquia.
            android.R.id.home -> {
                val intent = Intent(this, FilmListActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

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

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)

        listaPelis = mutableListOf()

        val listaPeliculas = findViewById<ListView>(R.id.listadoPeliculas)
        listaPelis = FilmDataSource.films
        adaptador = FilmArrayAdapter(this, R.layout.item_film, listaPelis)

        listaPeliculas.adapter = adaptador

        binding.listadoPeliculas.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        binding.listadoPeliculas.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                val inflater = mode.menuInflater
                inflater.inflate(R.menu.context_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.borrar -> {
                        borrarPeliculasSeleccionadas(mode)
                        mode.finish() // Cierra el modo de acción después de eliminar
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                selectedPositions.clear()
                adaptador.notifyDataSetChanged() // Actualizar el adaptador para limpiar la selección
            }

            override fun onItemCheckedStateChanged(
                mode: ActionMode, position: Int, id: Long, checked: Boolean) {

                if (checked) {
                    selectedPositions.add(position)
                } else {
                    selectedPositions.remove(position)
                }

                val count = selectedPositions.size
                mode.title = "$count seleccionados"
                adaptador.notifyDataSetChanged() // Actualizar el adaptador para reflejar los cambios
            }
        })


        binding.listadoPeliculas.setOnItemClickListener { _, _, posicion, _ ->
            val intent = Intent(this, FilmDataActivity::class.java).apply {
                putExtra("MODE", mode)
                putExtra("FILM_INDEX", posicion)
            }

            startActivity(intent)
        }
    }

    fun onClick(nuevaClase: Class<*>, flag: Int? = null) {
        val intent = Intent(this@FilmListActivity, nuevaClase)
        intent.putExtra("MODE", mode)

        if (flag != null){
            intent.flags = flag
        }

        startActivity(intent)
    }

    private fun anyadirPeli(){
        val nuevaPelicula = Film().apply {
            title = ""
            director = ""
            year = 0
            genre = Film.GENRE_ACTION
            format = Film.FORMAT_DIGITAL
            imdbUrl = null
            comments = null
        }

        listaPelis.add(nuevaPelicula)
        adaptador.notifyDataSetChanged()
    }

    private fun borrarPeliculasSeleccionadas(mode: ActionMode) {
        val posSeleccionada = binding.listadoPeliculas.checkedItemPositions
        val peliculasAEliminar = mutableListOf<Film>()

        for (i in 0 until posSeleccionada.size()) {
            val key = posSeleccionada.keyAt(i)
            if (posSeleccionada[key]) {
                peliculasAEliminar.add(listaPelis[key])
            }
        }

        listaPelis.removeAll(peliculasAEliminar)
        adaptador.notifyDataSetChanged()

        mode.finish()
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