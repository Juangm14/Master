package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment

class FilmDataFragment : Fragment() {

    private var filmIndex: Int = -1

    private val editFilmResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                Toast.makeText(requireContext(), getString(R.string.film_edited_success_msg), Toast.LENGTH_SHORT).show()
                filmIndex.let {
                    setFilmDetail(it, requireView())
                }
            }
            RESULT_CANCELED -> {
                Toast.makeText(requireContext(), getString(R.string.film_edit_cancel_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_film_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar el índice de película de los argumentos
        arguments?.let {
            filmIndex = it.getInt("FILM_INDEX", -1)
        }

        if (filmIndex != -1) {
            obtenerPelicula(view)
        }else{
            ocultarComponentes(view)
        }

        view.findViewById<Button>(R.id.verEnImdbBtn).setOnClickListener {
            val imdbUrl = FilmDataSource.films.getOrNull(filmIndex)?.imdbUrl
            if (imdbUrl != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                startActivity(intent)
            }
        }

        view.findViewById<Button>(R.id.backMainBtn).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.editPeliBtn).setOnClickListener {
            onClickEdit()
        }
    }

    private fun onClickEdit() {
        val intent = Intent(requireContext(), FilmEditActivity::class.java)
        intent.putExtra("MODE", Mode.Layouts)
        intent.putExtra("FILM_INDEX", filmIndex)
        editFilmResultLauncher.launch(intent)
    }

    fun setFilmDetail(position: Int, view: View) {
        filmIndex = position
        obtenerPelicula(view)
    }

    fun obtenerPelicula(view: View) {
        if (filmIndex == -1){
            ocultarComponentes(view)
            return
        }

        val selectedFilm = FilmDataSource.films[filmIndex]

        val generoPeli = Film.getGeneroString(requireContext(), selectedFilm.genre)
        val formatoPeli = Film.getFormatoString(requireContext(), selectedFilm.format)

        // Concatenar género y formato
        val generoFormato = when {
            generoPeli.isNotEmpty() && formatoPeli.isNotEmpty() -> "$generoPeli, $formatoPeli"
            generoPeli.isEmpty() -> formatoPeli
            else -> generoPeli
        }

        mostrarComponentes(view)

        view.findViewById<TextView>(R.id.peli_datos_label).text = selectedFilm.title
        view.findViewById<TextView>(R.id.director_data_label).text = selectedFilm.director
        view.findViewById<TextView>(R.id.anyo_data_label).text = selectedFilm.year.toString()
        view.findViewById<TextView>(R.id.genero_formato_label).text = generoFormato
        view.findViewById<TextView>(R.id.notas_data_label).text = selectedFilm.comments
        view.findViewById<ImageView>(R.id.posterFilm).setImageResource(selectedFilm.imageResId)
    }

    private fun mostrarComponentes(view: View) {
        view.findViewById<Button>(R.id.verEnImdbBtn).visibility = View.VISIBLE
        view.findViewById<ImageView>(R.id.posterFilm).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.peli_datos_label).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.director_data_label).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.anyo_data_label).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.genero_formato_label).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.notas_data_label).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.editPeliBtn).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.backMainBtn).visibility = View.VISIBLE
    }

    private fun ocultarComponentes(view: View) {
        view.findViewById<Button>(R.id.verEnImdbBtn).visibility = View.GONE
        view.findViewById<ImageView>(R.id.posterFilm).visibility = View.GONE
        view.findViewById<TextView>(R.id.peli_datos_label).visibility = View.GONE
        view.findViewById<TextView>(R.id.director_data_label).visibility = View.GONE
        view.findViewById<TextView>(R.id.anyo_data_label).visibility = View.GONE
        view.findViewById<TextView>(R.id.genero_formato_label).visibility = View.GONE
        view.findViewById<TextView>(R.id.notas_data_label).visibility = View.GONE
        view.findViewById<TextView>(R.id.editPeliBtn).visibility = View.GONE
        view.findViewById<TextView>(R.id.backMainBtn).visibility = View.GONE
    }
}



