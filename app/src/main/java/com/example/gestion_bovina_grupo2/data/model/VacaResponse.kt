package com.example.gestion_bovina_grupo2.data.model

import com.google.gson.annotations.SerializedName

data class VacaResponse(
    @SerializedName("_id")
    val id: String,
    val diio: Int,
    val dateBirthday: String,
    val genre: String,
    val race: String,
    val location: String,
    val sick: String?,
    val cowState: Boolean,
    @SerializedName("__v")
    val version: Int
)