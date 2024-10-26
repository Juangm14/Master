package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivitySelectorBinding


class SelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)

        binding = ActivitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.drawablesBtn.setOnClickListener {
            val intent = Intent(this, DrawablesActivity::class.java)
            startActivity(intent)
        }

        binding.customComponent.setOnClickListener{
            val intent = Intent(this, TextViewCitasActivity::class.java)
            startActivity(intent)
        }
    }
}