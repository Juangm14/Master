package es.ua.eps.filmoteca

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment

class FilmListFragment : ListFragment() {

    private var listener: OnFilmSelectedListener? = null

    interface OnFilmSelectedListener {
        fun onFilmSelected(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as OnFilmSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnFilmSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Obtener la lista de películas de FilmDataSource
        val films = FilmDataSource.films

        // Crear un ArrayAdapter para mostrar las películas en la lista
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            films.map { it.title }
        )

        // Asignar el adaptador a la lista del fragmento
        listAdapter = adapter

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        listener?.onFilmSelected(position)
    }
}

