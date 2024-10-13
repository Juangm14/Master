package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ArrayAdapter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.graphics.asImageBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.io.FileOutputStream


class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmEditBinding
    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 1001
    private var filmIndex: Int = -1

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

        filmIndex = intent.getIntExtra("FILM_INDEX", -1)

        val film = FilmDataSource.films[filmIndex]
        binding.editMovieTitle.setText(film.title)
        binding.editMovieDirector.setText(film.director)
        binding.editMovieYear.setText(film.year.toString())
        binding.editMovieComments.setText(film.comments)
        binding.editMovieImdb.setText(film.imdbUrl)
        binding.moviePoster.setImageResource(film.imageResId)

        val generos = resources.getStringArray(R.array.generos_peli)
        val adapterGeneros = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapterGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenre.adapter = adapterGeneros

        binding.spinnerGenre.setSelection(film.genre)

        val formatos = resources.getStringArray(R.array.formato_peli)
        val adapterFormatos = ArrayAdapter(this, android.R.layout.simple_spinner_item, formatos)
        adapterFormatos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFormat.adapter = adapterFormatos

        binding.spinnerFormat.setSelection(film.format)

        binding.guardarEditBtn.setOnClickListener {
            guardarDatos()
            setResult(RESULT_OK)
        }

        binding.cancelarEditBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.buttonTakePhoto.setOnClickListener {
//            val mb: Bitmap = Screenshot.hacerScreenshotView(imageView)
//            imageView.setImageBitmap(mb)
            capturarImagen()
        }

        binding.buttonSelectImage.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    private fun guardarDatos() {

        val film = FilmDataSource.films[filmIndex]

        // Actualiza los datos de la película
        film.title = binding.editMovieTitle.text.toString()
        film.director = binding.editMovieDirector.text.toString()
        film.year = binding.editMovieYear.text.toString().toIntOrNull() ?: 0
        film.comments = binding.editMovieComments.text.toString()
        film.imdbUrl = binding.editMovieImdb.text.toString()
        film.genre = Film.getGeneroNumero(this, binding.spinnerGenre.selectedItem.toString())
        film.format = Film.getFormatoNumero(binding.spinnerFormat.selectedItem.toString())

        // Notifica que los cambios se han guardado
        Toast.makeText(this, getString(R.string.film_edited_success_msg), Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()  // Regresa a FilmDataActivity
    }

    fun capturarImagen(){
        imageView = binding.moviePoster

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION)
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun guardarImagenEnAlmacenamientoInterno(bitmap: Bitmap): String? {
        val filename = "imagen_${System.currentTimeMillis()}.png"
        var fos: FileOutputStream? = null
        return try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            File(filesDir, filename).absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

                    CapturarSeleccionarImagen()

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

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun CapturarSeleccionarImagen() {
        val context = LocalContext.current
        var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

        // Launcher para capturar imágenes desde la cámara
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            imageBitmap = bitmap
        }

        // Launcher para seleccionar una imagen de la galería
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageBitmap = bitmap
            }
        }

        // Permiso para la cámara
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(50.dp)
                        .height(60.dp)
                        .padding(bottom = 16.dp, end = 8.dp)
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.icon_android),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(50.dp)
                    .height(60.dp)
                    .padding(bottom = 16.dp, end = 8.dp)
            )

            Button(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        cameraLauncher.launch(null)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                modifier = Modifier.padding(end = 4.dp).width(140.dp)
            ) {
                Text(text = stringResource(id = R.string.capturar_foto))
            }

            Button(
                onClick = {
                    galleryLauncher.launch("image/*")
                },
                modifier = Modifier.padding(end = 4.dp).width(140.dp)
            ) {
                Text(text = stringResource(id = R.string.seleccionar_img))
            }

            if (cameraPermissionState.status.shouldShowRationale) {
                Text(
                    text = "Se necesita permiso de cámara para capturar imágenes.",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }


    @Composable
    fun FilmEditForm() {
        val filmIndex = intent.getIntExtra("FILM_INDEX", -1)
        val context = LocalContext.current

        if (filmIndex != -1) {
            val film = FilmDataSource.films[filmIndex]

            var titulo by remember { mutableStateOf(film.title ?: "") }
            var director by remember { mutableStateOf(film.director ?: "") }
            var anyo by remember { mutableStateOf(film.year.toString()) }
            var enlaceimdb by remember { mutableStateOf(film.imdbUrl ?: "") }
            var notas by remember { mutableStateOf(film.comments ?: "") }

            var generoPeli = Film.getGeneroString(this,  film.genre)
            var formatoPeli = Film.getFormatoString(this, film.format)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TextField(
                    value = titulo,
                    onValueChange =
                    {
                        film.title = it
                        titulo = it
                    },
                    placeholder = { Text(text = stringResource(R.string.titulo_peli)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    singleLine = true,
                )

                TextField(
                    value = director,
                    onValueChange =
                    {
                        film.director = it
                        director = it
                    },
                    placeholder = { Text(text = stringResource(R.string.nombre_director)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    singleLine = true,
                )

                TextField(
                    value = anyo,
                    onValueChange =
                    {
                        film.year = it.toInt()
                        anyo = it
                    },
                    placeholder = { Text(text = stringResource(R.string.anyo_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    singleLine = true,
                )

                TextField(
                    value = enlaceimdb,
                    onValueChange =
                    {
                        film.imdbUrl = it
                        enlaceimdb = it
                    },
                    placeholder = { Text(text = stringResource(R.string.enlace_imdb)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    singleLine = true,
                )

                var expandidoGeneros by remember { mutableStateOf(false) }
                var expandidoFormatos by remember { mutableStateOf(false) }

                var generoSeleccionado by remember { mutableStateOf(generoPeli) }
                var formatoSeleccionado by remember { mutableStateOf(formatoPeli) }

                val generos = context.resources.getStringArray(R.array.generos_peli).toList()
                val formatos = context.resources.getStringArray(R.array.formato_peli).toList()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    OutlinedTextField(
                        value = generoSeleccionado,
                        onValueChange =
                        {
                            film.genre = Film.getGeneroNumero(context, it)
                            generoSeleccionado = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        trailingIcon = {
                            Icon(Icons.Default.KeyboardArrowDown, "",
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
                                film.genre = Film.getGeneroNumero(context, generoSeleccionado)
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
                        onValueChange =
                        {
                            film.format =  Film.getFormatoNumero(it)
                            formatoSeleccionado = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(Icons.Default.KeyboardArrowDown, "",
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
                                film.format =  Film.getFormatoNumero(formatoSeleccionado)
                                expandidoFormatos = false
                            }, text = { Text(label) })
                        }
                    }
                }

                TextField(
                    value = notas,
                    onValueChange =
                    {
                        film.comments = it
                        notas = it
                    },
                    placeholder = { Text(text = stringResource(R.string.comentarios)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
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