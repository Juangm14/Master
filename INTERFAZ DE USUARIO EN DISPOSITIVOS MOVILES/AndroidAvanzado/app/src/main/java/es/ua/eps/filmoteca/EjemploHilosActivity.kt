package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityEjemploHilosBinding

class EjemploHilosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEjemploHilosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEjemploHilosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Haciendo esto no se muestra nada y se carga cuando ha terminado.
//        var t = 10
//        do {
//            binding.tvCrono.text = "Contador: $t"
//            Thread.sleep(1000)
//            t--
//        } while (t > 0)
//
//        binding.tvCrono.text = "Contador terminado"

        binding.botonThread.setOnClickListener{
            val intent = Intent(this@EjemploHilosActivity, ThreadsActivity::class.java)
            startActivity(intent)
        }

        binding.botonAsyntask.setOnClickListener{
            val intent = Intent(this@EjemploHilosActivity, AsyncTaskActivity::class.java)
            startActivity(intent)
        }

        binding.botonCorrutinas.setOnClickListener{
            val intent = Intent(this@EjemploHilosActivity, ThreadsActivity::class.java)
            startActivity(intent)
        }
    }
}