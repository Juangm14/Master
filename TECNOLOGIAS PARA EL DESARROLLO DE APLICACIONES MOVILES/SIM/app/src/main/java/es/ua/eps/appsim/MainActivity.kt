package es.ua.eps.appsim

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.ua.eps.appsim.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val PERMISSION_REQUEST_READ_PHONE_STATE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimInfo.setOnClickListener {
            val permissionsNeeded = mutableListOf<String>()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
            }

            //Esas dos comprobaciones son para acceder a las celdas
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (permissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_READ_PHONE_STATE)
            } else {
                mostrarInfoSim()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            if (!(grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED })) {
                binding.info.text = "No se puede acceder a la información de la SIM."
            }
        }
    }

    private fun mostrarInfoSim() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            try {
                //NO PUEDO ACCEDER DESDE MI TELEFONO A ALGUNOS DATOS DE LA SIM PORQUE A PARTIR DE LA VERSIÓN 10 DE ANDROID SE BLOQUEA.
                //TUVE QUE INSTALAR UN DISPOSITIVO VIRTUAL CON MINSDK 24 Y CON VERSION 7.0 (para no ir justo)
                val simSerialNumber = telephonyManager.getSimSerialNumber()
                val operatorName = telephonyManager.getSimOperatorName()
                val subscriberId = telephonyManager.getSubscriberId()
                val networkType = telephonyManager.getNetworkType()
                val imei = telephonyManager.getDeviceId()
                val isoCountryNetwork = telephonyManager.getNetworkCountryIso()
                val isoCountrySim = telephonyManager.getSimCountryIso()
                val softwareVersion = telephonyManager.getDeviceSoftwareVersion()
                val voicemailNumber = telephonyManager.getVoiceMailNumber()
                val isRoaming = telephonyManager.isNetworkRoaming()

                val cellLocation = telephonyManager.getCellLocation()!! as? GsmCellLocation
                val cellId = cellLocation?.cid ?: "No está disponible"
                val lac = cellLocation?.lac ?: "No está disponible"

                val networkTypeStr = when (networkType) {
                    TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                    TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_UMTS -> "3G"
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE -> "2G"
                    else -> "Red desconocida"
                }

                binding.info.text = """
                    Detalle del teléfono:
                    
                    Estado de los Datos: CONECTADA
                    Tipo de Conexión: $networkTypeStr
                    IMEI: $imei
                    Operador de la red (físico): $operatorName
                    ID Suscriptor: $subscriberId
                    Número de Serie SIM: $simSerialNumber
                    Código ISO País Red: $isoCountryNetwork
                    Código ISO País SIM: $isoCountrySim
                    Versión Software IMEI: ${softwareVersion ?: "No disponible"}
                    Número del contestador: ${voicemailNumber ?: "No disponible"}
                    Tipo Red móvil: $networkTypeStr
                    Roaming activado: ${if (isRoaming) "Sí" else "No"}
                    Cell ID: $cellId
                    LAC: $lac
                """.trimIndent()
            } catch (e: Exception) {
                binding.info.text = "No se puede acceder a la información de la SIM."
            }
        } else {
            binding.info.text = "No se puede acceder a la información de la SIM."
        }
    }
}
