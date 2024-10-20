package es.ua.eps.filmoteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainFragmentActivity : AppCompatActivity(), FilmListFragment.OnFilmSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTablet()) {
            setContentView(R.layout.activity_list_data_large)
        } else {
            setContentView(R.layout.activity_list_fragment)

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FilmListFragment())
                    .commit()
            }
        }
    }

    private fun isTablet(): Boolean {
        return true
    }

    override fun onFilmSelected(position: Int) {
        var detailFragment = supportFragmentManager.findFragmentById(R.id.filmDataFragment) as? FilmDataFragment

        if (detailFragment != null) {
            detailFragment.setFilmDetail(position, detailFragment.requireView())
        } else {
            detailFragment = FilmDataFragment().apply {
                arguments = Bundle().apply {
                    putInt("FILM_INDEX", position)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
