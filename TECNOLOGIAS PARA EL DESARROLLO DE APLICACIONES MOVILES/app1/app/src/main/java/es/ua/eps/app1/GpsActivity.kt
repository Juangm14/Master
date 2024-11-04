package es.ua.eps.app1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import es.ua.eps.app1.databinding.ActivityGpsBinding
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback

class GpsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGpsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isPermisos = false
    private val CODIGO_PERMISO_UBICACION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        verificarPermisos()
        configurarLocationCallback()
    }

    private fun verificarPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), CODIGO_PERMISO_UBICACION)
        } else {
            isPermisos = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_PERMISO_UBICACION) {
            isPermisos = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isPermisos) {
                actualizarUbicacion()
            }
        }
    }

    // Esta función es necesario para coprobar si la aplicacion ya tiene permisos, no volver a solicitarlos porque muchas veces falla cerrando la aplicación automáticamente.
    override fun onResume() {
        super.onResume()
        if (isPermisos) {
            actualizarUbicacion()
        }
    }

    private fun configurarLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    binding.textLatitud.text = location.latitude.toString()
                    binding.textLongitud.text = location.longitude.toString()
                }
            }
        }
    }

    private fun actualizarUbicacion() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000L
        ).apply {
            setMinUpdateIntervalMillis(5000L)
        }.build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

