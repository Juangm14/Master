package es.ua.eps.filmoteca

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityEdicionBorrableBinding
import es.ua.eps.filmoteca.databinding.ActivityGraficaBinding

class GraficaActivity : AppCompatActivity() {
    private lateinit var grafica: Grafica

    private lateinit var binding: ActivityGraficaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafica)

        binding = ActivityGraficaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        grafica = binding.grafica

        val seekBar: SeekBar = findViewById(R.id.seek_bar_percentage)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                grafica.setPorcentaje(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}