package com.example.gestion_bovina_grupo2.data.model

import com.google.gson.annotations.SerializedName

data class VacaApi(
    @SerializedName("_id")
    val id: String,
    val diio: Int,
    val dateBirthday: String,
    val genre: String,
    val race: String,
    val location: String,
    val sick: String?,
    val cowState: Boolean
)

// Funciones para convertir a texto legible:
fun VacaApi.getGeneroLegible() = when(genre) {
    "F" -> "Hembra"
    "M" -> "Macho"
    else -> "Desconocido"
}