package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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