package es.ua.eps.app1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.app1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gpsBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, GpsActivity::class.java)
            startActivity(intent)
        }

        binding.acelerometroBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, AcelerometroActivity::class.java)
            startActivity(intent)
        }
    }
}