package es.ua.eps.filmoteca

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityNotificacionToastBinding
import es.ua.eps.filmoteca.databinding.ActivitySelectorBinding

class NotificacionToastActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotificacionToastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)

        binding = ActivityNotificacionToastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notifToast.setOnClickListener{

            val texto = if (binding.textoNotif.text.isNullOrBlank()) {
                "Escribe un texto"
            } else {
                binding.textoNotif.text.toString()
            }

            //Ponemos el input text vacío
            binding.textoNotif.setText("")

            val layout = layoutInflater.inflate(R.layout.toast_layout, null)

            layout!!.findViewById<TextView>(R.id.txtMensaje).text = texto

            val t3 = Toast(this@NotificacionToastActivity)
            t3.duration = Toast.LENGTH_SHORT
            t3.setGravity(Gravity.CENTER, 0, 0)
            t3.view = layout
            t3.show()


        }
    }
}