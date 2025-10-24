package com.example.gestion_bovina_grupo2.model

data class Usuario(
    val rut: String ="",
    val nombre: String ="",
    val apellido: String="",
    val email: String="",
    val password: String="",
    val rol: String="",
    val fono: Int? = null,
    val isValid: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)

data class UsuarioErrores(
    val email: String? = null,
    val password: String? = null
)
