package es.ua.eps.raw_filmoteca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging


class LoginActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "No se pudo obtener el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Token: $token")
        }


        // Configurar las opciones de inicio de sesión de Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .requestEmail()
            .build()

        // Crear el objeto GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configurar el botón de inicio de sesión
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    // Comprobar si ya hay un usuario identificado en onStart
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        handleAccount(account)
    }

    // Iniciar el flujo de inicio de sesión
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Manejar el resultado del inicio de sesión
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Procesar el resultado del inicio de sesión
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            handleAccount(account)
        } catch (e: ApiException) {
            Log.d("LoginActivity", "SignInResult Failed: ${e.statusCode}")
        }
    }

    // Manejar la cuenta del usuario autenticado
    private fun handleAccount(account: GoogleSignInAccount?) {
        if (account != null) {
            // Usuario autenticado correctamente con Google.
            // Aquí puedes guardar la cuenta en una variable global o iniciar la actividad principal.
            Log.d("GPS", "id: ${account.id}")
            Log.d("GPS", "idToken: ${account.idToken}")
            Log.d("GPS", "serverAuthCode: ${account.serverAuthCode}")
            Log.d("GPS", "givenName: ${account.givenName}")
            Log.d("GPS", "familyName: ${account.familyName}")
            Log.d("GPS", "displayName: ${account.displayName}")
            Log.d("GPS", "email: ${account.email}")
            Log.d("GPS", "photoUrl: ${account.photoUrl}")
            startActivity(Intent(this, FilmListActivity::class.java))
            finish()
        }
    }
}
