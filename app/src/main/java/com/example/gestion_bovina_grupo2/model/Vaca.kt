package com.example.gestion_bovina_grupo2.model

data class Vaca(
    val diio: Int,
    val fecha: String,
    val genero: String,
    val raza: String,
    val ubicacion: String,
    val enfermedades: String? = null,
    val fotoVaca: String? = null
)