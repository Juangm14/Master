package es.ua.eps.filmoteca

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityAsyncTaskBinding

class AsyncTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAsyncTaskBinding
    private lateinit var miTarea: MiCrono

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAsyncTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvCrono = findViewById<TextView>(R.id.tvCrono)
        miTarea = MiCrono(tvCrono)
        miTarea.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::miTarea.isInitialized) {
            miTarea.cancel(true)
        }
    }
}