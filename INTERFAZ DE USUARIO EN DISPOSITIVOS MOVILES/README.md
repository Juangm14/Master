# Primera version del proyecto
# Actualizado a la segunda versión

# Práctica 1.
## AboutActivity

- Creamos la nueva actividad About, pero se lo comento al tutor y me sugiere que en vez de llamar mediante un botón como tenia en la primera versión, que refactorice el nombre de la actividad Main.

## Aclaraciones 

- Para distinguir entre el modo **Layout** y **Compose**, he modificado la frase "*Creado por*" distinguiendo el modo **Compose**.

## Problema con las dependencias.

- Tuve un problema con las versiones que se solucionó en clase y finalmente se opta por dejar la versión 1.9.0 de kotlin y 1.5.1 hasta la 1.5.15 de compose.

- Las dependencias de los apuntes no se deben de sustituir por las del proyecto, tenemos que añadirlas a las existentes.

- Tras comprobar las versiones como son solo "Bug Fixes" se propone modificar la versión de Kotlin a la 1.9.25 y compose a la 1.5.15.

- Encontrado error en los apuntes en COMPOSE. La función de MyApp obligaba a pasarle content y realmente desde el initUI no se le pasa nada.

- Tuve unos problemas al añadir la imágen del logo con **Compose** porque inicialmente le estaba añadiendo padding a la imágen desde la etiqueta "*Creado por*" y cuando cambié de dispositivo a uno más grande no se quedaba en la esquina del dispositivo. Tuve que indicar que el padding fuera a top y start.

# Práctica 2

## Aclaraciones

- No he traducido 2 veces el texto de *"Ver película A"* y *"Ver película B"* sino lo que he realizado es traducir solo *"Ver plícula"* poniendo **"%1$s "** y en el el archivo *FilmListActivity* concatenar la traducción con el nombre de la película.

- En la función onclick en *FilmDataActivity* hacemos que el parámetro flag sea opcional para que no sea necesario pasarlo cuando no queramos emplearlo. De esta forma podemos asignar distinta flag para distintas actividades.

**No sé si existe la posibilidad de tener multiples flags, hay que tener en cuenta que en caso de poderse deberemos de cambiar el tipo de parámetro de Int? a [Int]**

- El enumerador se saca en un archivo diferente, de manera pública para que las demás clases puedan emplearlo. Antes era pública, pero al tenerla en otro archivo queda mejor estructurado y deja fuera de confusiones.

- La variable del enumerador  se cambia para la página nueva página principal que es ***FilmListActivity.kt*** para poder ir pasandolo de vista en vista y no hacer pública la variable.

- Se crea una nueva actividad ***ModeActivity.kt*** que se implementa mediante Compose, que tiene 2 botones que son los modos para inicializar *Filmoteca*. 
  Los modos son *Iniciar Filmoteca con Layouts* y *Iniciar Filmoteca con Compose*. De esta forma no tenemos que modificar el código para lanzar un Modo u Otro, sino retrocedemos con el movil una actividad y seleccionamos el modo.

# Práctica 3

- En la creación de *ToolBar* se intentó mediante [Scaffold](https://developer.android.com/develop/ui/compose/components/app-bars?hl=es-419) pero está en fase experimental. Por ello, se decidió optar por la que no está en fase experimental que es [AndroidView](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/views-in-compose?hl=es-419) permitiendo la creación de un *Toolbar* de manera muy sencilla.

- Para la creación de los dropdown con Compose me he basado en una pagina de [GeeksForGeeks](https://www.geeksforgeeks.org/drop-down-menu-in-android-using-jetpack-compose/) y otra de alguna persona que es la que realmente me ha funcionado [DropDownMenu JetpackCompose](https://alexzh.com/jetpack-compose-dropdownmenu/)

- En compose no se mete texto en algunos campos en la edición para que se vean los placeholders.

- En esta práctica me confundí y pensaba que era tomar captura de patantalla. Lo voy a dejar comentado igualmente porque me costo encontrar como se hacía (Está en el archivo *EditFilmActivity.kt*).

- Se añade el siguiente codigo para que me permita acceder a los permisos mediante Compose ya que se hace de distinta forma a layouts porque no se puede modificar la imagen facilmente.

      `implementation "com.google.accompanist:accompanist-permissions:<version>"`

