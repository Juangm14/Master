import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.filmoteca.Film
import es.ua.eps.filmoteca.R


class FilmArrayAdapterRecycler(val films: List<Film>) : RecyclerView.Adapter<FilmArrayAdapterRecycler.ViewHolder>() {

    var listener: ((Film) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.film_title)
        private val director: TextView = view.findViewById(R.id.film_director)
        private val posterFilm: ImageView = view.findViewById(R.id.film_image)

        fun bind(film: Film, clickListener: ((Film) -> Unit)?) {
            title.text = film.title
            director.text = film.director
            posterFilm.setImageResource(film.imageResId)

            itemView.setOnClickListener {
                clickListener?.invoke(film) // Pasamos el objeto Film al listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_film, parent, false)
        val holder = ViewHolder(v)

        v.setOnClickListener {
            val position: Int = holder.adapterPosition
            listener?.let { it1 -> it1(films[position]) }
        }

        return holder
    }

    override fun getItemCount(): Int = films.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(films[position], listener)
    }

    fun setOnItemClickListener(listener: (Film) -> Unit) {
        this.listener = listener
    }
}

