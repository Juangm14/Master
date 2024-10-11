package es.ua.eps.filmoteca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class FilmArrayAdapter(
    context: Context,
    resource: Int,
    objects: List<Film>
) : ArrayAdapter<Film>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_film, parent, false)

        val tvTitle = view.findViewById<TextView>(R.id.film_title)
        val tvDirector = view.findViewById<TextView>(R.id.film_director)
        val ivImage = view.findViewById<ImageView>(R.id.film_image)

        val film = getItem(position)

        film?.let {
            tvTitle.text = it.title
            tvDirector.text = it.director
            ivImage.setImageResource(it.imageResId)
        }

        return view
    }
}
