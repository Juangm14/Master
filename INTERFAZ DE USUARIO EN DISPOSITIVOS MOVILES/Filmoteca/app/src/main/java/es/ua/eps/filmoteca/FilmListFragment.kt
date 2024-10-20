package es.ua.eps.filmoteca

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.ListFragment

class FilmListFragment : ListFragment() {

    private var callback: OnFilmSelectedListener? = null
    private lateinit var adaptador: FilmArrayAdapter
    private var films: MutableList<Film> = mutableListOf()

    interface OnFilmSelectedListener {
        fun onFilmSelected(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = try {
            context as OnFilmSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnFilmSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        films = FilmDataSource.films.toMutableList()
        adaptador = FilmArrayAdapter(requireContext(), R.layout.activity_list_item, films)
        listAdapter = adaptador

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun updateFilmList(newFilms: List<Film>) {
        films.clear()
        films.addAll(newFilms)
        adaptador.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        updateFilmList(FilmDataSource.films)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        callback?.onFilmSelected(position)
    }
}


