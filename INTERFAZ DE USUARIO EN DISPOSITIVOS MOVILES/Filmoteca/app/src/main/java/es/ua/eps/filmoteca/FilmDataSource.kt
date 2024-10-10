package es.ua.eps.filmoteca

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf<Film>()

    init {
        var f = Film()
        f.title = "Regreso al futuro"
        f.director = "Robert Zemeckis"
        f.imageResId = R.mipmap.ic_launcher
        f.comments = ""
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_SCIFI
        f.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f.year = 1985
        films.add(f)

        f = Film()
        f.title = "Spider-Man"
        f.director = "Sam Raimi"
        f.imageResId = R.mipmap.ic_launcher // Cambia esto por la imagen correspondiente
        f.comments = "Presenta la historia de origen de Peter Parker y su lucha contra el Duende Verde."
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_ACTION // Cambia a GENRE_SUPERHERO si lo prefieres
        f.imdbUrl = "http://www.imdb.com/title/tt0145487"
        f.year = 2002
        films.add(f)

        f = Film()
        f.title = "Spider-Man 2"
        f.director = "Sam Raimi"
        f.imageResId = R.mipmap.ic_launcher // Cambia esto por la imagen correspondiente
        f.comments = "Considerada una de las mejores secuelas de superhéroes, introduce a Doctor Octopus."
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_ACTION // Cambia a GENRE_SUPERHERO si lo prefieres
        f.imdbUrl = "http://www.imdb.com/title/tt0316654"
        f.year = 2004
        films.add(f)

        f = Film()
        f.title = "Spider-Man 3"
        f.director = "Sam Raimi"
        f.imageResId = R.mipmap.ic_launcher // Cambia esto por la imagen correspondiente
        f.comments = "Introduce al simbiótico Venom y explora el lado oscuro de Peter Parker."
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_ACTION // Cambia a GENRE_SUPERHERO si lo prefieres
        f.imdbUrl = "http://www.imdb.com/title/tt0413300"
        f.year = 2007
        films.add(f)

        f = Film()
        f.title = "The Amazing Spider-Man"
        f.director = "Marc Webb"
        f.imageResId = R.mipmap.ic_launcher // Cambia esto por la imagen correspondiente
        f.comments = "Reinventa la historia de Spider-Man con un enfoque más oscuro y emocional."
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_ACTION // Cambia a GENRE_SUPERHERO si lo prefieres
        f.imdbUrl = "http://www.imdb.com/title/tt0948470"
        f.year = 2012
        films.add(f)

        f = Film()
        f.title = "The Amazing Spider-Man 2"
        f.director = "Marc Webb"
        f.imageResId = R.mipmap.ic_launcher // Cambia esto por la imagen correspondiente
        f.comments = "Presenta la relación de Peter con Gwen Stacy y introduce a varios villanos."
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_ACTION // Cambia a GENRE_SUPERHERO si lo prefieres
        f.imdbUrl = "http://www.imdb.com/title/tt2585572"
        f.year = 2014
        films.add(f)
    }
}