package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmEditBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilmEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.guardarEditBtn.setOnClickListener {
            finish()
        }

        binding.cancelarEditBtn.setOnClickListener {
            finish()
        }
    }
}