package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import es.ua.eps.filmoteca.databinding.ActivityTextViewCitasBinding
import es.ua.eps.filmoteca.databinding.SnackbarLayoutBinding

class NotificacionSnackBarActivity : AppCompatActivity() {

    private var ultimaTarea: String? = null
    private lateinit var binding: SnackbarLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.snackbar_layout)

        binding = SnackbarLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAgregar.setOnClickListener {
            val textoTarea = binding.edtTarea.text.toString()

            if (textoTarea.isBlank()) {
                Snackbar.make(it, "Escribe un texto", Snackbar.LENGTH_SHORT).show()
            } else {
                ultimaTarea = textoTarea
                binding.txtListaTareas.append("$textoTarea\n")
                binding.edtTarea.text.clear()

                Snackbar.make(it, "Tarea añadida", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        binding.txtListaTareas.text = binding.txtListaTareas.text.toString().removeSuffix("$textoTarea\n")
                        ultimaTarea = null
                    }.show()
            }
        }
    }
}