package es.ua.eps.filmoteca

import android.os.Bundle
import android.util.TypedValue
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityCuadroDialogoBinding
import es.ua.eps.filmoteca.databinding.SnackbarLayoutBinding

class CuadroDialogoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCuadroDialogoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.snackbar_layout)

        binding = ActivityCuadroDialogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectColor.setOnClickListener{
            mostrarDialogoColor()
        }

        binding.selectSize.setOnClickListener {
            mostrarDialogoSize();
        }
    }

    fun mostrarDialogoColor(){
        val inflater = layoutInflater
        val customLayout = inflater.inflate(R.layout.custom_color_dialogo, null)

        val dialog = AlertDialog.Builder(this)
            .setView(customLayout)
            .setPositiveButton("Aceptar") { _, _ ->
                val radioGroupColor = customLayout.findViewById<RadioGroup>(R.id.radioGroupColor)

                if (radioGroupColor.checkedRadioButtonId == R.id.radioBlancoNegro) {
                    setColor(R.color.white, R.color.black)
                }else if (radioGroupColor.checkedRadioButtonId == R.id.radioNegroBlanco) {
                    setColor(R.color.black, R.color.white)
                }else if(radioGroupColor.checkedRadioButtonId == R.id.radioNegroVerde) {
                    setColor(R.color.black, R.color.custom_green)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    fun setColor(colorBackground: Int, color: Int){
        val bgColor = ContextCompat.getColor(this, colorBackground)
        val textColor = ContextCompat.getColor(this, color)
        binding.textExample.setBackgroundColor(bgColor)
        binding.textExample.setTextColor(textColor)
    }

    fun mostrarDialogoSize(){
        val inflater = layoutInflater
        val customLayout = inflater.inflate(R.layout.custom_size_dialog, null)

        val dialog = AlertDialog.Builder(this)
            .setView(customLayout)
            .setPositiveButton("Aceptar") { _, _ ->
                val radioGroupTamaño = customLayout.findViewById<RadioGroup>(R.id.radioGroupTamaño)

                val tamaño = when (radioGroupTamaño.checkedRadioButtonId) {
                    R.id.radioPequeño -> 8f
                    R.id.radioNormal -> 12f
                    R.id.radioGrande -> 20f
                    else -> 12f
                }

                binding.textExample.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamaño)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}