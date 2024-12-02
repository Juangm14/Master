package es.ua.eps.sharedpreferences

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import es.ua.eps.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

         binding.buttonApply.setOnClickListener {
            val texto = binding.editText.text.toString()

            val editor = sharedPreferences.edit()
            editor.putString("pref_text", Base64.encodeToString(texto.toByteArray(), Base64.DEFAULT))
            editor.apply()
        }

        binding.buttonClose.setOnClickListener {
            finish()
        }

        binding.buttonApply.setOnClickListener {
            val nuevoTexto = binding.editText.text.toString()

            val editor = sharedPreferences.edit()
            editor.putString("texto", nuevoTexto)
            editor.apply()

            actualizarRectangulo()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, KotlinSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actualizarRectangulo() {
        val texto = sharedPreferences.getString("texto", "Texto por defecto")
        val colorTexto = sharedPreferences.getString("text_color", "#000000") ?: "#000000"
        val colorFondo = sharedPreferences.getString("background_color", "#FFFFFF") ?: "#FFFFFF"
        val alpha = sharedPreferences.getInt("alpha", 255)
        val rotacion = sharedPreferences.getInt("rotation", 0)
        val bold = sharedPreferences.getBoolean("bold", false)
        val italic = sharedPreferences.getBoolean("italic", false)
        val sizeText = sharedPreferences.getString("text_size", "16")?.toFloat() ?: 16f

        binding.rectanguloTexto.apply {
            text = texto
            setTextColor(
                when (colorTexto) {
                    "Verde" -> Color.parseColor("#008000")
                    "Azul" -> Color.parseColor("#0000FF")
                    "Amarillo" -> Color.parseColor("#FFFF00")
                    "Rojo" -> Color.parseColor("#FF0000")
                    "Negro" -> Color.parseColor("#000000")
                    else -> Color.parseColor(colorTexto) // Usar el color por defecto si no coincide
                }
            )
            setBackgroundColor(
                when (colorFondo) {
                    "Verde" -> Color.parseColor("#008000")
                    "Azul" -> Color.parseColor("#0000FF")
                    "Amarillo" -> Color.parseColor("#FFFF00")
                    "Rojo" -> Color.parseColor("#FF0000")
                    "Negro" -> Color.parseColor("#000000")
                    else -> Color.parseColor(colorFondo) // Usar el color por defecto si no coincide
                }
            )
            this.alpha =  (255 - alpha) / 255f
            rotation = rotacion.toFloat()
            textSize = sizeText

            val style = when {
                bold && italic -> android.graphics.Typeface.BOLD_ITALIC
                bold -> android.graphics.Typeface.BOLD
                italic -> android.graphics.Typeface.ITALIC
                else -> android.graphics.Typeface.NORMAL
            }
            setTypeface(typeface, style)
        }
    }

}
