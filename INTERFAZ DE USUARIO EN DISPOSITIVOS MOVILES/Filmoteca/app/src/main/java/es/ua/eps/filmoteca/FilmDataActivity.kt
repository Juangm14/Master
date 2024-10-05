package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmDataActivity : AppCompatActivity() {

    companion object {
        val EXTRA_FILM_TITLE = "EXTRA_FILM_TITLE"
    }

    private lateinit var binding: ActivityFilmDataBinding

    private val editFilmResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //Se han guardado los cambios en la edición de la película correctamente.
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, getString(R.string.film_edited_success_msg), Toast.LENGTH_SHORT).show()
            //Se ha cancelado la edición de la película.
        } else if (result.resultCode == RESULT_CANCELED) {
            Toast.makeText(this, getString(R.string.film_edit_cancel_msg), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.peliDatosLabel.setText(getString(R.string.datos_peli_label) + " " + EXTRA_FILM_TITLE)

        binding.verPeliRelacBtn.setOnClickListener {
            onClick(FilmDataActivity::class.java)
        }

        binding.editPeliBtn.setOnClickListener {
            val intent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
            editFilmResultLauncher.launch(intent)
        }

        binding.backMainBtn.setOnClickListener {
            onClick(FilmListActivity::class.java, flag = Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

    }

    //Hacemos que el parámetro flag sea opcional para que no sea necesario pasarlo cuando no queramos emplearlo.
    fun onClick(nuevaClase: Class<*>, flag: Int? = null) {
        val intent = Intent(this@FilmDataActivity, nuevaClase)

        if (flag != null){
            intent.flags = flag
        }

        startActivity(intent)

    }
}