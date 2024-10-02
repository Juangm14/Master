package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

    fun onClick(nuevaClase: Class<*>) {
        val intent = Intent(this@FilmListActivity, nuevaClase)
        startActivity(intent)
    }
}