package com.example.gestion_bovina_grupo2.model

data class Vaca(
    val diio: String = "",
    val fecha: String = "",
    val genero: String = "",
    val raza: String = "",
    val ubicacion: String = "",
    val enfermedades: String = "",
    val fotoVaca: String? = null,
    val erroresVaca: VacaErrores = VacaErrores()
)

data class VacaErrores(
    val diio: String? = null,
    val fecha: String? = null,
    val genero: String? = null,
    val raza: String? = null,
    val ubicacion: String? = null,
    val enfermedades: String? = null,
    val fotoVaca: String? = null
)