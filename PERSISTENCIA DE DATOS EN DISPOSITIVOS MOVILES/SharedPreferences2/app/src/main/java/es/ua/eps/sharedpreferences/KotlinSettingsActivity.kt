package es.ua.eps.sharedpreferences

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class KotlinSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()
            // Registra el listener cuando el fragmento está activo
            preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            // Desregistra el listener cuando el fragmento no esté activo
            preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key != null) {
                val preference = findPreference<Preference>(key)
                when (key) {
                    "text_color" -> {
                        val newColor = sharedPreferences?.getString(key, "#000000")
                        preference?.summary = newColor
                    }
                    "text_size" -> {
                        val newSize = sharedPreferences?.getString(key, "16")
                        preference?.summary = "$newSize px"
                    }
                    "background_color" -> {
                        val newColor = sharedPreferences?.getString(key, "#FFFFFF")
                        preference?.summary = newColor
                    }
                    "alpha" -> {
                        val alphaValue = sharedPreferences?.getInt(key, 255)
                        preference?.summary = "$alphaValue"
                    }
                    "rotation" -> {
                        val rotationValue = sharedPreferences?.getInt(key, 0)
                        preference?.summary = "$rotationValue°"
                    }
                }
            }
        }
    }
}


