package es.ua.eps.sharedpreferences

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.sharedpreferences.databinding.ActivityShowKotlinSharedPreferencesBinding

class ShowKotlinSharedPreferences : AppCompatActivity() {
    private lateinit var binding: ActivityShowKotlinSharedPreferencesBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowKotlinSharedPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE)

        val texto = sharedPreferences.getString(
            "texto",
            Base64.encodeToString("texto por defecto".toByteArray(), Base64.DEFAULT)
        )
        val tamaño = sharedPreferences.getString(
            "tamaño",
            Base64.encodeToString("32".toByteArray(), Base64.DEFAULT)
        )

        binding.textViewDisplayed.text = String(Base64.decode(texto, Base64.DEFAULT))
        binding.textViewDisplayed.textSize = String(Base64.decode(tamaño, Base64.DEFAULT)).toFloat()

        binding.buttonClose.setOnClickListener { finish() }
    }
}
