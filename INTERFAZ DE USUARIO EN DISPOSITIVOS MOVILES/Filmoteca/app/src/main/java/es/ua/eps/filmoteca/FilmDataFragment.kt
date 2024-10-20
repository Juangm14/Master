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
import androidx.fragment.app.Fragment

class FilmDataFragment : Fragment() {

    private var filmIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_film_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el botón para abrir IMDB
        view.findViewById<Button>(R.id.verEnImdbBtn).setOnClickListener {
            val imdbUrl = FilmDataSource.films[filmIndex].imdbUrl
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
            startActivity(intent)
        }
    }

    // Método que se llamará para establecer los detalles de la película
    fun setFilmDetail(position: Int, view: View) {
        filmIndex = position
        obtenerPelicula(view)
    }

    private fun obtenerPelicula(view: View) {
        if (filmIndex == -1) return

        val selectedFilm = FilmDataSource.films[filmIndex]

        val generoPeli = Film.getGeneroString(requireContext(), selectedFilm.genre)
        val formatoPeli = Film.getFormatoString(requireContext(), selectedFilm.format)

        // Concatenar género y formato
        val generoFormato = when {
            generoPeli.isNotEmpty() && formatoPeli.isNotEmpty() -> "$generoPeli, $formatoPeli"
            generoPeli.isEmpty() -> formatoPeli
            else -> generoPeli
        }

        // Establecer los datos en los TextViews
        view.findViewById<TextView>(R.id.peli_datos_label).text = selectedFilm.title
        view.findViewById<TextView>(R.id.director_data_label).text = selectedFilm.director
        view.findViewById<TextView>(R.id.anyo_data_label).text = selectedFilm.year.toString()
        view.findViewById<TextView>(R.id.genero_formato_label).text = generoFormato
        view.findViewById<TextView>(R.id.notas_data_label).text = selectedFilm.comments
        view.findViewById<ImageView>(R.id.posterFilm).setImageResource(selectedFilm.imageResId)
    }
}



