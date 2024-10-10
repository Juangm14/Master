package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import es.ua.eps.filmoteca.databinding.ActivityFilmEditBinding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.drawToBitmap
import com.google.android.gms.cast.framework.media.ImagePicker

class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmEditBinding
    private lateinit var imageView: ImageView

    private var mode: Mode = Mode.Layouts

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.moviePoster.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mode = intent.getSerializableExtra("MODE", Mode::class.java) ?: Mode.Layouts

        when (mode) {
            Mode.Layouts -> initLayouts()
            Mode.Compose -> setContent { initCompose() }
        }
    }

    private fun initLayouts() {
        binding = ActivityFilmEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.guardarEditBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.cancelarEditBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.buttonTakePhoto.setOnClickListener {
            imageView = binding.moviePoster

            val mb: Bitmap = Screenshot.hacerScreenshotView(imageView)
            imageView.setImageBitmap(mb)
        }

        binding.buttonSelectImage.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    companion object Screenshot {
        private fun hacerScreenShot(view: View) : Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val b = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return b
        }

        fun hacerScreenshotView(v: View): Bitmap {
            return hacerScreenShot(v.rootView)
        }
    }

    // MEDIANTE JETPACK COMPOSE
    @Composable
    private fun initCompose() {
        setContent {
            Surface {
                MyWindow {
                    AndroidView(
                        factory = { content ->
                            Toolbar(content).apply {
                                setTitle(R.string.app_name)
                                setTitleTextColor(
                                    ContextCompat.getColor(
                                        content,
                                        android.R.color.white
                                    )
                                )
                                setBackgroundColor(
                                    ContextCompat.getColor(
                                        content,
                                        R.color.customPurple
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.icon_android),
                            contentDescription = "Imagen de la película",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(50.dp)
                                .height(60.dp)
                                .padding(bottom = 16.dp, end = 8.dp)
                        )

                        val context = LocalContext.current

                        Button(
                            onClick = {
                                Toast.makeText(context, R.string.func_no_imp, Toast.LENGTH_SHORT)
                                    .show()
                            }, modifier = Modifier
                                .padding(end = 4.dp)
                        ) {
                            Text(text = stringResource(id = R.string.capturar_foto))
                        }

                        Button(
                            onClick = {
                                Toast.makeText(context, R.string.func_no_imp, Toast.LENGTH_SHORT)
                                    .show()
                            }, modifier = Modifier
                                .padding(end = 4.dp)
                        ) {
                            Text(text = stringResource(id = R.string.seleccionar_img))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FilmEditForm()

                    Row {
                        MyButton(
                            onClick = { onClickGuardar() },
                            text = stringResource(id = R.string.guardar_btn),
                            modifier = Modifier
                                .width(178.dp)
                                .padding(top = 40.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        MyButton(
                            onClick = { onClickCancelar() },
                            text = stringResource(id = R.string.cancelar_btn),
                            modifier = Modifier
                                .width(178.dp)
                                .padding(top = 40.dp)
                        )

                    }
                }
            }
        }
    }

    @Composable
    fun FilmEditForm() {
        var titulo by remember { mutableStateOf("") }
        var director by remember { mutableStateOf("Robert Zemeckis") }
        var anyo by remember { mutableStateOf("1985") }
        var enlaceimdb by remember { mutableStateOf("https://www.imdb.com/title/tt0088763") }
        var notas by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                placeholder = {  Text( text = stringResource(R.string.titulo_peli)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                singleLine = true,
            )

            TextField(
                value = director,
                onValueChange = { director = it },
                placeholder = { Text( text = stringResource(R.string.nombre_director)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                singleLine = true,
            )

            TextField(
                value = anyo,
                onValueChange = { anyo = it },
                placeholder = { Text( text = stringResource(R.string.anyo_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                singleLine = true,
            )

            TextField(
                value = enlaceimdb,
                onValueChange = { enlaceimdb = it },
                placeholder = { Text( text = stringResource(R.string.enlace_imdb)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                singleLine = true,
            )

            var expandidoGeneros by remember { mutableStateOf(false) }
            var expandidoFormatos by remember { mutableStateOf(false) }

            var generoSeleccionado by remember { mutableStateOf("Selecciona un género") }
            var formatoSeleccionado by remember { mutableStateOf("Selecciona un formato") }

            val generos = resources.getStringArray(R.array.generos_peli).toList()
            val formatos = resources.getStringArray(R.array.formato_peli).toList()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                OutlinedTextField(
                    value = generoSeleccionado,
                    onValueChange = { generoSeleccionado = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown,"",
                            Modifier.clickable { expandidoGeneros = !expandidoGeneros })
                    }
                )
                DropdownMenu(
                    expanded = expandidoGeneros,
                    onDismissRequest = { expandidoGeneros = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                ) {
                    generos.forEach { label ->
                        DropdownMenuItem(onClick = {
                            generoSeleccionado = label
                            expandidoGeneros = false
                        }, text = { Text(label) })
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                OutlinedTextField(
                    value = formatoSeleccionado,
                    onValueChange = { formatoSeleccionado = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Default.KeyboardArrowDown,"",
                            Modifier.clickable { expandidoFormatos = !expandidoFormatos })
                    }
                )

                DropdownMenu(
                    expanded = expandidoFormatos,
                    onDismissRequest = { expandidoFormatos = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    formatos.forEach { label ->
                        DropdownMenuItem(onClick = {
                            formatoSeleccionado = label
                            expandidoFormatos = false
                        }, text = { Text(label) })
                    }
                }
            }

            TextField(
                value = notas,
                onValueChange = { notas = it },
                placeholder = { Text( text = stringResource(R.string.comentarios)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
    }

    @Composable
    fun MyWindow(content: @Composable ColumnScope.() -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            content()
        }
    }

    @Composable
    fun MyColumn(content: @Composable ColumnScope.() -> Unit) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            content()
        }
    }

    @Composable
    fun MyButton(onClick: () -> Unit, text: String, modifier: Modifier) {
        Button(
            onClick = { onClick() },
            modifier = modifier
        ) {
            Text(text = text)
        }
    }

    private fun onClickGuardar() {
        setResult(RESULT_OK)
        finish()
    }

    private fun onClickCancelar() {
        setResult(RESULT_CANCELED)
        finish()
    }
}