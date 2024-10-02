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
