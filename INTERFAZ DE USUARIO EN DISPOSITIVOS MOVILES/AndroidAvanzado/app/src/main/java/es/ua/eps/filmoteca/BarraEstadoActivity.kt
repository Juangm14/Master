package es.ua.eps.filmoteca

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.filmoteca.databinding.ActivityBarraEstadoBinding
import es.ua.eps.filmoteca.databinding.ActivityGraficaBinding

class BarraEstadoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBarraEstadoBinding;
    val CHANNEL_ID = "channel_id"
    val permission = android.Manifest.permission.POST_NOTIFICATIONS
    val REQUEST_PERMISSION_CODE = 1
    private var contador = 0
    private val NOTIF_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarraEstadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        solicitarPermiso()

        binding.iniciarBtn.setOnClickListener{
            contador++
            mostrarNotificacion()
        }

        binding.detenerBtn.setOnClickListener{
            detenerNotificacion()
        }
    }

    private fun detenerNotificacion(){
        if (contador > 0) {
            contador--

            if (contador == 0) {
                NotificationManagerCompat.from(this).cancel(NOTIF_ID)
            } else {
                mostrarNotificacion()
            }
        }
    }

    private fun solicitarPermiso() {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Mostrar explicación adicional
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION_CODE)
            }
        }
    }

    private fun mostrarNotificacion(){
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {

            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android)
                .setContentTitle("Tareas en progreso")
                .setContentText("Tareas iniciadas: $contador")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)

            val intent = Intent(this, BarraEstadoActivity::class.java)

            val pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            builder.setContentIntent(pi)

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(NOTIF_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal ejemplo"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = "Descripcion del canal de ejemplo"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso ha sido concedido!!
            } else {
                // El permiso ha sido denegado :(
            }
        }
    }

}