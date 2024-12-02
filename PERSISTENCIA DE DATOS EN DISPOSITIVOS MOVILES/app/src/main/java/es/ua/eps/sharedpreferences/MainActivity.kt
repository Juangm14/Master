package es.ua.eps.sharedpreferences

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE)

        // Recuperar el texto y el tamaño desde las preferencias
        val textoBase64 = sharedPreferences.getString("texto", "")
        val tamañoBase64 = sharedPreferences.getString("tamaño", "")

        // Decodificar el texto y tamaño desde Base64
        val texto = textoBase64?.let { String(Base64.decode(it, Base64.DEFAULT)) } ?: "Texto por defecto"
        val tamaño = tamañoBase64?.let { String(Base64.decode(it, Base64.DEFAULT)) } ?: "Tamaño por defecto"

        // Mostrar los valores en los TextViews
        binding.actualTexto.text = "Texto actual: $texto"
        binding.sizeTexto.text = "Tamaño actual: $tamaño"

        binding.logoButton.setImageResource(R.drawable.ic_java_logo)

         binding.buttonApply.setOnClickListener {
            val texto = binding.editText.text.toString()
            val tamaño = binding.seekBar.progress

            val editor = sharedPreferences.edit()
            editor.putString("texto", Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT))
            editor.putString("tamaño", Base64.encodeToString(tamaño.toString().toByteArray(), Base64.DEFAULT))
            editor.apply()
        }


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textView.text = "Tamaño: $progress"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.buttonClose.setOnClickListener { finish() }

        binding.logoButton.setOnClickListener {
            startActivity(Intent(this, JavaMainActivity::class.java))
            finish();
        }


        binding.buttonApply.setOnClickListener {
            val nuevoTexto = binding.editText.text.toString()
            val tamaño = binding.seekBar.progress.toString()

            val editor = sharedPreferences.edit()
            editor.putString("texto", Base64.encodeToString(nuevoTexto.toByteArray(), Base64.DEFAULT))
            editor.putString(
                "tamaño",
                Base64.encodeToString(tamaño.toString().toByteArray(), Base64.DEFAULT)
            )
            editor.apply()

            binding.actualTexto.text = "Texto actual: $nuevoTexto"
            binding.sizeTexto.text = "Tamaño actual: $tamaño"

            startActivity(Intent(this, ShowKotlinSharedPreferences::class.java))
        }
    }
}
