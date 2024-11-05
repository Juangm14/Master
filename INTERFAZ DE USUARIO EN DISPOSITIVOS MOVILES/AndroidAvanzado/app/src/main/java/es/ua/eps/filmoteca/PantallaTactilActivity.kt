package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityPantallaTactilBinding

class PantallaTactilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaTactilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantallaTactilBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}