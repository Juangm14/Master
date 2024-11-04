package es.ua.eps.appsim

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
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
                //val iccid = telephonyManager.simSerialNumber
                val operatorName = telephonyManager.simOperatorName
                //val imsi = telephonyManager.subscriberId
                val networkType = telephonyManager.networkType
                //val imei = telephonyManager.deviceId
                val networkOperator = telephonyManager.networkOperatorName
                val isoCountryNetwork = telephonyManager.networkCountryIso
                val isoCountrySim = telephonyManager.simCountryIso
                val softwareVersion = telephonyManager.deviceSoftwareVersion
                val voicemailNumber = telephonyManager.voiceMailNumber
                val isRoaming = telephonyManager.isNetworkRoaming

                val networkTypeStr = when (networkType) {
                    TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                    TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_UMTS -> "3G"
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE -> "2G"
                    else -> "Desconocido"
                }

                binding.info.text = """
                    Estado de los Datos:
                    Tipo de Conexión: $networkTypeStr
                    IMEI:
                    Operador de la red (físico): $operatorName
                    ID Suscriptor: 
                    Número de Serie SIM:
                    Código ISO País Red: $isoCountryNetwork
                    Código ISO País SIM: $isoCountrySim
                    Versión Software IMEI: ${softwareVersion ?: "No disponible"}
                    Número del contestador: ${voicemailNumber ?: "No disponible"}
                    Tipo Red móvil: $networkTypeStr
                    Roaming activado: ${if (isRoaming) "Sí" else "No"}
                """.trimIndent()
            } catch (e: Exception) {
                binding.info.text = "No se puede acceder a la información de la SIM."
            }
        } else {
            binding.info.text = "No se puede acceder a la información de la SIM."
        }
    }
}
