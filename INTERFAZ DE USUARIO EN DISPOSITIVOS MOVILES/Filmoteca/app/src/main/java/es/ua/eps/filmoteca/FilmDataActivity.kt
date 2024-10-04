package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmDataActivity : AppCompatActivity() {

    companion object {
        val EXTRA_FILM_TITLE = "EXTRA_FILM_TITLE"
    }

    private lateinit var binding: ActivityFilmDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.peliDatosLabel.setText(getString(R.string.datos_peli_label) + " " + EXTRA_FILM_TITLE)

        binding.verPeliRelacBtn.setOnClickListener {
            onClick(FilmDataActivity::class.java)
        }

        binding.editPeliBtn.setOnClickListener {
            onClick(FilmEditActivity::class.java, result = true)
        }

        binding.backMainBtn.setOnClickListener {
            onClick(FilmListActivity::class.java, flag = Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

    }

    //Hacemos que el parámetro flag sea opcional para que no sea necesario pasarlo cuando no queramos emplearlo.
    fun onClick(nuevaClase: Class<*>, flag: Int? = null, result: Boolean? = false) {
        val intent = Intent(this@FilmDataActivity, nuevaClase)
        if (flag != null){
            intent.flags = flag
        }

        startActivity(intent)

    }
}