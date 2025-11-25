package com.example.gestion_bovina_grupo2.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VacaApi(
    @SerializedName("_id")
    val id: String,
    val diio: Long,
    val dateBirthday: String,
    val genre: String,
    val race: String,
    val location: String,
    val sick: String?,
    val cowState: Boolean
): Parcelable

// Funciones para convertir a texto legible:
fun VacaApi.getGeneroLegible() = when(genre) {
    "F" -> "Hembra"
    "M" -> "Macho"
    else -> "Desconocido"
}