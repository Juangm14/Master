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
        wifiInfo = wifiManager.getConnectionInfo()

        val ssid = wifiInfo.getSSID()
        val bssid = wifiInfo.getBSSID()
        val linkSpeed = wifiInfo.getLinkSpeed()
        val frequency = wifiInfo.getFrequency()
        val rssi = wifiInfo.getRssi()

        val ipAddress = wifiInfo.getIpAddress()

        val ipString = String.format(
            "%d.%d.%d.%d",
            (ipAddress and 0xFF),
            (ipAddress shr 8 and 0xFF),
            (ipAddress shr 16 and 0xFF),
            (ipAddress shr 24 and 0xFF)
        )

        val dhcpInfo = wifiManager.getDhcpInfo()
        val puertaEnlace = String.format(
            "%d.%d.%d.%d",
            (dhcpInfo.gateway and 0xFF),
            (dhcpInfo.gateway shr 8 and 0xFF),
            (dhcpInfo.gateway shr 16 and 0xFF),
            (dhcpInfo.gateway shr 24 and 0xFF)
        )

        //No disponible a partir de versión superior a API 21.
//        val mascaraRed = String.format(
//            "%d.%d.%d.%d",
//            (dhcpInfo.netmask and 0xFF),
//            (dhcpInfo.netmask shr 8 and 0xFF),
//            (dhcpInfo.netmask shr 16 and 0xFF),
//            (dhcpInfo.netmask shr 24 and 0xFF)
//        )

        val mascaraRed = obtenerMascaraSubred()

        binding.valueIp.text = ipString

        binding.valueSsid.text = ssid
        binding.valueBssid.text = bssid
        binding.valueLinkSpeed.text = "${linkSpeed} Mbps"
        binding.valueFrequency.text = "${frequency} MHz"
        binding.valueRssi.text = "${rssi} dBm"
        binding.puertaEnlace.text = "${puertaEnlace}"
        binding.mascaraRed.text = "${mascaraRed}"

    }

    //Una vez obtenidas las propiedades del enlace de la red activa, obtenemos la longitud del prefijo de la dirección IP
    // Calculamos la máscara de subred con un numero entero de 32 bits.
    //Extraemos de en octeto en octeto los bits y los convertimos a formato direccion IP (x.x.x.x)
    private fun obtenerMascaraSubred(): String {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(network)

        linkProperties?.linkAddresses?.forEach { linkAddress ->
            val prefixLength = linkAddress.prefixLength

            val mask = (0xffffffff.toInt() shl (32 - prefixLength)).toLong()
            val netmask = String.format(
                "%d.%d.%d.%d",
                (mask shr 24 and 0xFF),
                (mask shr 16 and 0xFF),
                (mask shr 8 and 0xFF),
                (mask and 0xFF)
            )
            return netmask
        }
        return "No disponible"
    }

}
