package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import com.example.gestion_bovina_grupo2.data.model.LoginRequest
import com.example.gestion_bovina_grupo2.data.remote.RetrofitInstance

/**
 * Este repositorio se encarga de acceder a los datos usando Retrofit
 * Simplifica la lógica y separa responsabilidades
 */
class UsuarioRepository(private val context: Context) {

    /**
     * Función que obtiene los datos desde la API
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Token JWT si es exitoso, null si hay error
     */
    suspend fun login(email: String, password: String): String? {
        return try {
            // Crear el objeto de petición
            val loginRequest = LoginRequest(email, password)

            // Hacer la petición a la API
            val response = RetrofitInstance.api.login(loginRequest)

            // Guardar el token en SharedPreferences
            guardarToken(response.token)

            // Retornar el token
            response.token

        } catch (e: Exception) {
            // Si hay error, imprimir en consola y retornar null
            println("Error en login: ${e.message}")
            null
        }
    }

    /**
     * Guarda el token en SharedPreferences
     */
    private fun guardarToken(token: String) {
        val sharedPreferences = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("auth_token", token)
            putBoolean("is_authenticated", true)
            apply()
        }
    }

    /**
     * Obtiene el token guardado
     */
    fun obtenerToken(): String? {
        val sharedPreferences = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    /**
     * Verifica si hay sesión activa
     */
    fun estaAutenticado(): Boolean {
        val sharedPreferences = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_authenticated", false)
    }

    /**
     * Cierra la sesión (limpia el token)
     */
    fun cerrarSesion() {
        val sharedPreferences = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            remove("auth_token")
            putBoolean("is_authenticated", false)
            apply()
        }
    }
}