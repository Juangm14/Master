package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AboutActivity : AppCompatActivity() {

    private var mode: Mode = Mode.Layouts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {

        mode = intent.getSerializableExtra("MODE", Mode::class.java) ?: Mode.Layouts

        when (mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> setContent { initCompose() }
        }
    }

    //Interfaz de la forma tradicional mediante XML.
    private fun initLayouts() {
        setContentView(R.layout.activity_about)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)

        val sitioweb_btn = findViewById<android.widget.Button>(R.id.sitioweb_btn);

        sitioweb_btn.setOnClickListener{
            onClickGoToWeb()
        }

        val soporte_btn = findViewById<android.widget.Button>(R.id.soporte_btn)

        soporte_btn.setOnClickListener{
           onClickSupport()
        }

        val volver_btn = findViewById<android.widget.Button>(R.id.volver_btn)

        volver_btn.setOnClickListener{
            onClickBack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpTo(this, Intent(this, FilmListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @Composable
    private fun initCompose() {
        setContent {
            Surface{
                MyWindow {
                    AndroidView(
                    factory = { ctx ->
                        Toolbar(ctx).apply {
                            setTitle(R.string.app_name)
                            setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                            setBackgroundColor(ContextCompat.getColor(ctx, R.color.customPurple))

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )

                    MyColumn {
                        Text(
                            text = stringResource(id = R.string.creado_por_compose),
                            modifier = Modifier.padding(8.dp)
                        )
                        MyImage(
                            painter = painterResource(id = R.drawable.about_creator_picture),
                            contentDescription = stringResource(id = R.string.creado_por_compose),
                            modifier = Modifier.size(210.dp, 320.dp).padding(bottom = 32.dp)
                        )
                        MyButton({ onClickGoToWeb() }, stringResource(id = R.string.sitioweb_btn))
                        MyButton({ onClickSupport() }, stringResource(id = R.string.soporte_btn))
                        MyButton({ onClickBack() }, stringResource(id = R.string.volver_btn))
                    }
                }
            }
        }
    }

    @Composable
    fun MyWindow(content: @Composable ColumnScope.() -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }

    @Composable
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            content()
        }
    }

    @Composable
    fun MyImage(painter: Painter, contentDescription: String, modifier: Modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    fun MyButton(onClick: () -> Unit, text: String,) {
        Button(
            onClick = { onClick() },
            modifier = Modifier.width(210.dp).padding(bottom = 8.dp),
        ) {
            Text(text = text)
        }
    }

    private fun onClickGoToWeb() {
        val viewIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse("http://www.midominio.com"))

        if (viewIntent.resolveActivity(packageManager) != null) {
            startActivity(viewIntent)
        }else{
            Toast.makeText(this, R.string.func_no_imp, Toast.LENGTH_LONG).show()
        }
    }

    private fun onClickSupport() {
        val viewIntent = Intent(Intent.ACTION_SENDTO,
            Uri.parse("mailto:midireccion@dominio.com"))

        if (viewIntent.resolveActivity(packageManager) != null) {
            startActivity(viewIntent)
        }else{
            Toast.makeText(this, R.string.func_no_imp, Toast.LENGTH_LONG).show()
        }
    }

    private fun onClickBack() {
        finish()
    }
}