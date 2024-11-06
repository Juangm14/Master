package es.ua.eps.app3

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.ua.eps.app3.databinding.ActivityMainBinding
import android.Manifest
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var wifiManager: WifiManager
    private lateinit var wifiInfo: WifiInfo
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            obtenerInformacionWifi()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                obtenerInformacionWifi()
            } else {
                binding.labelNoPermisos.text = "El permiso no fue concedido."
                binding.labelNoPermisos.setTextColor(Color.RED)
                binding.labelNoPermisos.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun obtenerInformacionWifi() {
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiInfo = wifiManager.connectionInfo

        val ssid = wifiInfo.ssid
        val bssid = wifiInfo.bssid
        val linkSpeed = wifiInfo.linkSpeed
        val frequency = wifiInfo.frequency
        val rssi = wifiInfo.rssi

        val ipAddress = wifiInfo.ipAddress

        val ipString = String.format(
            "%d.%d.%d.%d",
            (ipAddress and 0xFF),
            (ipAddress shr 8 and 0xFF),
            (ipAddress shr 16 and 0xFF),
            (ipAddress shr 24 and 0xFF)
        )

        binding.valueIp.text = ipString

        binding.valueSsid.text = ssid
        binding.valueBssid.text = bssid
        binding.valueLinkSpeed.text = "${linkSpeed} Mbps"
        binding.valueFrequency.text = "${frequency} MHz"
        binding.valueRssi.text = "${rssi} dBm"
    }
}
