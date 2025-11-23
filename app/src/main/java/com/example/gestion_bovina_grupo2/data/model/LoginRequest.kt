package com.example.gestion_bovina_grupo2.data.model

/**
 * Modelo para la petición de login
 */
data class LoginRequest(
    val email: String,
    val password: String
)