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
import android.net.Uri
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toolbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat

import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding

class FilmDataActivity : AppCompatActivity() {

    companion object {
        val EXTRA_FILM_TITLE = "EXTRA_FILM_TITLE"
    }

    private var mode: Mode = Mode.Layouts
    private lateinit var binding: ActivityFilmDataBinding
    private var filmIndex: Int = -1

    private val editFilmResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, getString(R.string.film_edited_success_msg), Toast.LENGTH_SHORT).show()
            when (mode) {
                Mode.Layouts -> obtenerPelicula()
                Mode.Compose -> setContent { initCompose() }
            }
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

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)

        filmIndex = intent.getIntExtra("FILM_INDEX", -1)

        obtenerPelicula()

        binding.editPeliBtn.setOnClickListener {
            onClickEdit();
        }

        binding.backMainBtn.setOnClickListener {
            onClick(FilmListActivity::class.java, flag = Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpTo(this, Intent(this, FilmListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtenerPelicula(){
        val selectedFilm = FilmDataSource.films[filmIndex]

        var generoPeli = Film.getGeneroString(this,  selectedFilm.genre)
        var formatoPeli = Film.getFormatoString(this, selectedFilm.format)

        //Para concatenar la "," con el formato encaso de que tenta o que no tenga género
        if(generoPeli.isNotEmpty() && formatoPeli.isNotEmpty()){
            generoPeli += ", " + formatoPeli
        }else if(generoPeli.isEmpty()){
            generoPeli = formatoPeli
        }

        binding.directorLabel.text = getString(R.string.anyo_label)
        binding.anyoLabel.text = getString(R.string.director_label)
        binding.peliDatosLabel.text = selectedFilm.title
        binding.directorDataLabel.text = selectedFilm.director
        binding.anyoDataLabel.text = selectedFilm.year.toString()
        binding.generoFormatoLabel.text = generoPeli
        binding.notasDataLabel.text = selectedFilm.comments
        binding.posterFilm.setImageResource(selectedFilm.imageResId)

        binding.verEnImdbBtn.setOnClickListener {
            val imdbUrl = selectedFilm.imdbUrl
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
            startActivity(intent)
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

    @Composable
    private fun initCompose() {

        filmIndex = intent.getIntExtra("FILM_INDEX", -1)

        if (filmIndex != -1) {
            val film = FilmDataSource.films[filmIndex]

            var generoPeli = Film.getGeneroString(this,  film.genre)
            var formatoPeli = Film.getFormatoString(this, film.format)

            if(generoPeli.isNotEmpty() && formatoPeli.isNotEmpty()){
                generoPeli += ", " + formatoPeli
            }else if(generoPeli.isEmpty()){
                generoPeli = formatoPeli
            }

            Surface {
                MyWindow {
                    AndroidView(
                        factory = { ctx ->
                            Toolbar(ctx).apply {
                                setTitle(R.string.app_name)
                                setTitleTextColor(
                                    ContextCompat.getColor(
                                        ctx,
                                        android.R.color.white
                                    )
                                )
                                setBackgroundColor(
                                    ContextCompat.getColor(
                                        ctx,
                                        R.color.customPurple
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )

                    MyColumn {
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        ) {
                            // Columna con la imagen y el botón de editar
                            Column(
                                modifier = Modifier
                                    .width(168.dp)
                                    .padding(end = 16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = film.imageResId),
                                    contentDescription = "Imagen de la película",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(168.dp)
                                        .height(212.dp)
                                        .padding(bottom = 16.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 8.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = film.title ?: "",
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(bottom = 12.dp)
                                )

                                Text(
                                    text = getString(R.string.director_label) + ":",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(
                                    text = film.director ?: "",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )

                                Text(
                                    text = getString(R.string.anyo_label) + ":",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(
                                    text = film.year.toString(),
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )

                                Text(
                                    text = generoPeli,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )

                                MyButton(
                                    onClick = {
                                        val imdbUrl = film.imdbUrl
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                                        startActivity(intent)
                                    },
                                    text = stringResource(id = R.string.relac_peli_btn),
                                    modifier = Modifier
                                        .width(178.dp)
                                        .padding(bottom = 8.dp)
                                )
                            }
                        }

                        Text(
                            text = film.comments ?: "",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(170.dp)
                                    .padding(end = 4.dp)
                            ) {
                                MyButton(
                                    onClick = { onClickBack() },
                                    text = stringResource(id = R.string.volver_peli),
                                    modifier = Modifier
                                        .width(178.dp)
                                )
                            }


                            Column(
                                modifier = Modifier
                                    .width(170.dp)
                                    .padding(end = 4.dp)
                            ) {
                                MyButton(
                                    onClick = { onClickEdit() },
                                    text = stringResource(id = R.string.edit_peli_btn),
                                    modifier = Modifier
                                        .width(178.dp)
                                        .padding(end = 4.dp)
                                )
                            }
                        }
                    }
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
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            content()
        }
    }

    @Composable
    fun MyButton(onClick: () -> Unit, text: String, modifier: Modifier) {
        Button(
            onClick = { onClick() },
            modifier = modifier
        ) {
            Text(text = text)
        }
    }

    private fun onClickEdit() {
        val intent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
        intent.putExtra("MODE", mode)
        intent.putExtra("FILM_INDEX", filmIndex)
        editFilmResultLauncher.launch(intent)
    }

    private fun onClickBack() {
        finish()
    }

}