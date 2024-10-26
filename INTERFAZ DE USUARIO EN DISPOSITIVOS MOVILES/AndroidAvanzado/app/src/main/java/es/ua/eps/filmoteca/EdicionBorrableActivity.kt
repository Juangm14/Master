package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityEdicionBorrableBinding

class EdicionBorrableActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEdicionBorrableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edicion_borrable)

        binding = ActivityEdicionBorrableBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}