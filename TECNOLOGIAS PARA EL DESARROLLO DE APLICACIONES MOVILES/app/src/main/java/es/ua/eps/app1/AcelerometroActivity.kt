package es.ua.eps.app1

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.hardware.Sensor;
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import android.widget.Toast
import es.ua.eps.app1.databinding.ActivityAcelerometroBinding
import kotlin.math.sqrt

class AcelerometroActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityAcelerometroBinding

    private lateinit var sensorManager: SensorManager
    private var sensorAcelerometro: Sensor? = null

    private lateinit var xAxis : TextView
    private lateinit var yAxis : TextView
    private lateinit var zAxis : TextView
    private lateinit var aceleracion : TextView

    private var umbral: Float = 1.5f
    private var ultimaLectura: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcelerometroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        xAxis = binding.valorX
        yAxis = binding.valorY
        zAxis = binding.valorZ
        aceleracion = binding.aceleracion

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        if (sensorAcelerometro == null){
            Toast.makeText(this, "acelerometro no disponible", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSensorChanged(event :SensorEvent) {
        val sensor = event.sensor

        if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val aceleracionActual = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val aceleracionSinGravedad = aceleracionActual - SensorManager.GRAVITY_EARTH
            aceleracion.setText("Acc: " + String.format("%.2f", aceleracionSinGravedad))

            val tiempoActual: Long = System.currentTimeMillis()

            if ((tiempoActual - ultimaLectura) > 100) {
                if (aceleracionSinGravedad > umbral) {
                    ultimaLectura = tiempoActual

                    xAxis.text = "x: " + String.format("%.2f", x)
                    yAxis.text = "y: " + String.format("%.2f", y)
                    zAxis.text = "z: " + String.format("%.2f", z)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sensorAcelerometro != null) {
            sensorManager.registerListener(this, sensorAcelerometro!!, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this, "Acelerómetro no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //Not implemented
    }
}