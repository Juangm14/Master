package es.ua.eps.raw_filmoteca

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import es.ua.eps.raw_filmoteca.data.Film
import es.ua.eps.raw_filmoteca.data.FilmDataSource
import es.ua.eps.raw_filmoteca.data.FilmsArrayAdapter
import es.ua.eps.raw_filmoteca.databinding.ActivityFilmListBinding
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

//-------------------------------------
class FilmListActivity : BaseActivity()
    , AdapterView.OnItemClickListener {


    private lateinit var googleSignInClient: GoogleSignInClient


    private lateinit var bindings : ActivityFilmListBinding
    private lateinit var filmAdapter: FilmsArrayAdapter

    //---------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        checkPermission(Manifest.permission.INTERNET, {
            filmAdapter.notifyDataSetChanged()
        })


        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Token de FCM: $token")
            } else {
                Log.e("FCM_TOKEN", "No se pudo obtener el token")
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("movies")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM_SUB", "Suscrito al tema movies")
                } else {
                    Log.e("FCM_SUB", "Error al suscribirse al tema")
                }
            }

        val title = intent.getStringExtra("title")
        title?.let {
            Log.d("NOTIF","Ha llegado uinformacion en una push: ${it}")
        }

        val addFilmButton: Button = findViewById(R.id.addFilm)
        addFilmButton.setOnClickListener {
            // Acción para agregar película
            showAddFilmDialog()
        }
    }

    //Para mostrar el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val account = GoogleSignIn.getLastSignedInAccount(this)
                if (account != null) {
                    mostrarInfoCuenta(account)
                } else {
                    mostrarMensaje("No hay ninguna cuenta autenticada.")
                }
                true
            }
            R.id.action_sign_out -> {
                signOut()
                true
            }
            R.id.action_disconnect -> {
                disconnectAccount()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //---------------------------------
    override fun onRestart() {
        super.onRestart()
        filmAdapter.notifyDataSetChanged()
    }

    //---------------------------------
    private fun initUI() {
        bindings = ActivityFilmListBinding.inflate(layoutInflater)
        with(bindings) {
            setContentView(root)
            filmAdapter = FilmsArrayAdapter(this@FilmListActivity, android.R.layout.simple_list_item_1, FilmDataSource.films)
            list.onItemClickListener = this@FilmListActivity
            list.adapter = filmAdapter
        }
    }

    //---------------------------------
    // AdapterView.OnItemClickListener (ListView)
    //---------------------------------
    override fun onItemClick(adapterView: AdapterView<*>?, view: View?, index: Int, l: Long) {
        val intent = Intent(this, FilmDataActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        intent.putExtra(FilmDataActivity.EXTRA_FILM_ID, index)
        startActivity(intent)
    }

    private fun mostrarInfoCuenta(account: GoogleSignInAccount) {
        val info = """
            Nombre: ${account.displayName}
            Correo: ${account.email}
            ID: ${account.id}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Información de la Cuenta")
            .setMessage(info)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun mostrarMensaje(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            mostrarMensaje("Sesión cerrada.")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun disconnectAccount() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            mostrarMensaje("Cuenta desconectada.")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showAddFilmDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_film, null)
        builder.setView(dialogView)

        val filmTitleEditText: EditText = dialogView.findViewById(R.id.editTextFilmTitle)
        val filmDirectorEditText: EditText = dialogView.findViewById(R.id.editTextFilmDirector)
        val filmYearEditText: EditText = dialogView.findViewById(R.id.editTextFilmYear)
        val filmImageUrlEditText: EditText = dialogView.findViewById(R.id.editTextFilmImageUrl)
        val filmCommentsEditText: EditText = dialogView.findViewById(R.id.editTextFilmComments)
        val formatSpinner: Spinner = dialogView.findViewById(R.id.spinnerFilmFormat)
        val genreSpinner: Spinner = dialogView.findViewById(R.id.spinnerFilmGenre)
        val filmImdbUrlEditText: EditText = dialogView.findViewById(R.id.editTextFilmImdbUrl)

        val formatAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Film.Format.values().map { it.name }
        )
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        formatSpinner.adapter = formatAdapter

        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Film.Genre.values().map { it.name }
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter

        builder.setTitle("Añadir Nueva Película")
            .setPositiveButton("Añadir") { _, _ ->
                val title = filmTitleEditText.text.toString()
                val director = filmDirectorEditText.text.toString()
                val year = filmYearEditText.text.toString().toIntOrNull()
                val imageUrl = filmImageUrlEditText.text.toString()
                val comments = filmCommentsEditText.text.toString()
                val format = formatSpinner.selectedItemPosition
                val genre = genreSpinner.selectedItemPosition
                val imdbUrl = filmImdbUrlEditText.text.toString()

                if (title.isNotEmpty() && director.isNotEmpty() && year != null && imageUrl.isNotEmpty() && comments.isNotEmpty()) {
                    val newFilm = Film().apply {
                        this.title = title
                        this.director = director
                        this.year = year
                        this.imageUrl = imageUrl
                        this.comments = comments
                        this.format = Film.Format.values()[format] // Asegúrate de mapear correctamente a tu enum
                        this.genre = Film.Genre.values()[genre] // Lo mismo para el género
                        this.imdbUrl = imdbUrl
                    }

                    val existingFilmIndex = FilmDataSource.films.indexOfFirst { it.title == title }

                    if (existingFilmIndex != -1) {
                        // Si la película existe, actualizarla
                        val existingFilm = FilmDataSource.films[existingFilmIndex]
                        existingFilm.director = director
                        existingFilm.year = year
                        existingFilm.imageUrl = imageUrl
                        existingFilm.comments = comments
                        existingFilm.format = Film.Format.values()[format]
                        existingFilm.genre = Film.Genre.values()[genre]
                        existingFilm.imdbUrl = imdbUrl
                        Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show()
                    } else {
                        FilmDataSource.films.add(newFilm)
                        Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
                    }

                    filmAdapter.notifyDataSetChanged()

                    sendPushNotification(title)

                    Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun getAccessToken(): String {
        var token = ""
        Thread {
            try {
                val inputStream = assets.open("serviceAccountFile.json")
                val credentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                credentials.refreshIfExpired()
                token = credentials.accessToken.tokenValue
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        return token
    }

    private fun sendPushNotification(filmTitle: String) {
        val accessToken = getAccessToken()  // Obtener el token de acceso OAuth 2.0
        val json = """
    {
      "to": "movies",
      "notification": {
        "title": "Nueva película añadida",
        "body": "Se ha agregado la película: $filmTitle"
      }
    }
    """.trimIndent()

        val request = Request.Builder()
            .url("https://fcm.googleapis.com/v1/projects/filmoteca-12964/messages:send")  // Usa la URL v1
            .post(json.toRequestBody("application/json".toMediaType()))
            .header("Authorization", "Bearer $accessToken")  // Usa el token de acceso
            .header("Content-Type", "application/json")
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM_PUSH", "Error al enviar notificación", e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("FCM_PUSH", "Notificación enviada: ${response.body?.string()}")
            }
        })
    }
}
