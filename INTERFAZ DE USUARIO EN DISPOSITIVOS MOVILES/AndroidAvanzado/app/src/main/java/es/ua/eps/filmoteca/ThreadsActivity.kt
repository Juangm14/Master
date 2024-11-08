package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityEjemploHilosBinding
import es.ua.eps.filmoteca.databinding.ActivityThreadsBinding

class ThreadsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThreadsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThreadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var t = 10
        // En vez de heredar de Thread lo realizo con la forma alternativa.
        Thread {
            while (t > 0) {
                runOnUiThread { binding.tvCrono.text = "Contador Threads: $t" }
                Thread.sleep(1000)
                t--
            }
            runOnUiThread { binding.tvCrono.text = "Contador terminado" }
        }.start()
    }
}