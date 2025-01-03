package es.ua.eps.sqlite.data

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val nombreCompleto: String,
    val email: String
)