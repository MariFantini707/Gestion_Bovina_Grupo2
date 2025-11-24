package com.example.gestion_bovina_grupo2.data.model

data class VacaRequest(
    val diio: Int,
    val dateBirthday: String,
    val genre: String,
    val race: String,
    val location: String,
    val sick: String?
)

