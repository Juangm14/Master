package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivitySelectorBinding
import es.ua.eps.filmoteca.databinding.ActivityTextViewCitasBinding

class TextViewCitasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextViewCitasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view_citas)

        binding = ActivityTextViewCitasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}