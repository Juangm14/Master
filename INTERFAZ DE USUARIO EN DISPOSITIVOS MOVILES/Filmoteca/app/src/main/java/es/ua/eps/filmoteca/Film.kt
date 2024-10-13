package es.ua.eps.filmoteca

import android.content.Context

class Film {
    var imageResId = 0 // Propiedades de la clase
    var title: String? = null
    var director: String? = null
    var year = 0
    var genre = 0
    var format = 0
    var imdbUrl: String? = null
    var comments: String? = null

    override fun toString(): String {
        return title?:"<Sin titulo>"
    }

    companion object {
        const val FORMAT_DVD = 0 // Formatos
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2
        const val GENRE_ACTION = 0 // Géneros
        const val GENRE_COMEDY = 1
        const val GENRE_DRAMA = 2
        const val GENRE_SCIFI = 3
        const val GENRE_HORROR = 4

        fun getGeneroString(context: Context, genero: Int) : String {
            return when(genero){
                GENRE_ACTION -> context.getString(R.string.accion_gen)
                GENRE_COMEDY -> context.getString(R.string.comedia_gen)
                GENRE_DRAMA -> context.getString(R.string.drama_gen)
                GENRE_SCIFI -> context.getString(R.string.scifi_gen)
                GENRE_HORROR -> context.getString(R.string.terror_gen)
                else -> ""
            }
        }

        fun getGeneroNumero(context: Context, generoStr: String): Int {

            var seleccionado = -1

            seleccionado = when (generoStr) {
                context.getString(R.string.accion_gen) ->  GENRE_ACTION
                context.getString(R.string.comedia_gen) -> GENRE_COMEDY
                context.getString(R.string.drama_gen) -> GENRE_DRAMA
                context.getString(R.string.scifi_gen) -> GENRE_SCIFI
                context.getString(R.string.terror_gen) -> GENRE_HORROR
                else -> -1
            }

            return seleccionado;
        }


        fun getFormatoString(context: Context, formato: Int) : String {
            return when(formato){
                FORMAT_DVD -> "DVD"
                FORMAT_BLURAY -> "BluRay"
                FORMAT_DIGITAL -> "Digital"
                else -> ""
            }
        }

        fun getFormatoNumero(formatoStr: String): Int {
            val seleccionado = when (formatoStr) {
                "DVD" -> FORMAT_DVD
                "Blu-Ray" -> FORMAT_BLURAY
                "Digital" -> FORMAT_DIGITAL
                else -> -1 // Devuelve -1 si el formato no es reconocido
            }
            return seleccionado
        }

    }
}