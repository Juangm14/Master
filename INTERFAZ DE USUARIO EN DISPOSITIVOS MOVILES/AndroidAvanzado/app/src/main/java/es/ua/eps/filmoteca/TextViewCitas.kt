package es.ua.eps.filmoteca

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextViewCitas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    private var citas = arrayOf(
        "No hay que ir para atrás ni para darse impulso (Lao Tsé)",
        "No hay caminos para la paz; la paz es el camino (Mahatma Gandhi)",
        "Haz el amor y no la guerra (John Lennon)",
        "Para trabajar basta estar convencido de una cosa: que trabajar es menos aburrido que divertirse (Charles Baudelaire)",
        "Lo peor que hacen los malos es obligarnos a dudar de los buenos (Jacinto Benavente)",
      " Las guerras seguirán mientras el color de la piel siga siendo más importante que el de los ojos (Bob Marley)",
        "Aprende a vivir y sabrás morir bien (Confucio)"
    )

    init {
        mostrarCita()

        setOnClickListener {
            mostrarCita()
        }
    }

    private fun mostrarCita() {
        text = citas.random()
    }
}