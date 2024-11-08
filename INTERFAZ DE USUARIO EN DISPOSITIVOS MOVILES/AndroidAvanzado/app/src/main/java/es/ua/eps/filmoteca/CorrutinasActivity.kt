package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityCorrutinasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CorrutinasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorrutinasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCorrutinasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            var t = 10
            while (t > 0) {
                launch(Dispatchers.Main) {
                    binding.tvCrono.text = "Contador Corrutinas: $t"
                    t--
                }
                delay(1000)
            }
            binding.tvCrono.text = "Contador terminado"
        }
    }
}