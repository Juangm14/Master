package es.ua.eps.filmoteca

import android.os.AsyncTask
import android.widget.TextView

class MiCrono(private val tvCrono: TextView) : AsyncTask<Void, Int, Void>() {

    override fun onPreExecute() {
        tvCrono.text = "Contador: 10"
    }

    override fun doInBackground(vararg params: Void?): Void? {
        var t = 10
        while (t > 0 && !isCancelled) {
            publishProgress(t)
            Thread.sleep(1000)
            t--
        }
        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        tvCrono.text = "Contador: ${values[0]}"
    }

    override fun onPostExecute(result: Void?) {
        tvCrono.text = "Contador terminado"
    }

    override fun onCancelled() {
        tvCrono.text = "Contador cancelado"
    }
}
