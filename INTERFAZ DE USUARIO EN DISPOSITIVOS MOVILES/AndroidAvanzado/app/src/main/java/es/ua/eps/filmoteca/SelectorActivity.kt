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

        binding.customEditText.setOnClickListener{
            val intent = Intent(this, EdicionBorrableActivity::class.java)
            startActivity(intent)
        }

        binding.customGrafica.setOnClickListener{
            val intent = Intent(this, GraficaActivity::class.java)
            startActivity(intent)
        }

        binding.customNotificaciones.setOnClickListener{
            val intent = Intent(this, NotificacionToastActivity::class.java)
            startActivity(intent)
        }

        binding.customNotifSnackbar.setOnClickListener{
            val intent = Intent(this, NotificacionSnackBarActivity::class.java)
            startActivity(intent)
        }

        binding.notifDialog.setOnClickListener{
            val intent = Intent(this, CuadroDialogoActivity::class.java)
            startActivity(intent)
        }

        binding.barraEstado.setOnClickListener{
            val intent = Intent(this, BarraEstadoActivity::class.java)
            startActivity(intent)
        }

        binding.pantallaTactil.setOnClickListener{
            val intent = Intent(this, PantallaTactilActivity::class.java)
            startActivity(intent)
        }

        binding.estilosMain.setOnClickListener {
            val intent = Intent(this, EstilosMainActivity::class.java)
            startActivity(intent)
        }

        binding.ejemploHilos.setOnClickListener {
            val intent = Intent(this, EjemploHilosActivity::class.java)
            startActivity(intent)
        }

    }
}