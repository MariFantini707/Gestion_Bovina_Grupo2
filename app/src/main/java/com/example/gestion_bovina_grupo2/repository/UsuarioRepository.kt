package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import com.example.gestion_bovina_grupo2.data.local.TokenDataStore
import com.example.gestion_bovina_grupo2.data.model.LoginRequest
import com.example.gestion_bovina_grupo2.data.remote.RetrofitInstance

/**
 * Este repositorio se encarga de acceder a los datos usando Retrofit
 * y gestionar el token con DataStore
 */
class UsuarioRepository(private val context: Context) {

    private val tokenDataStore = TokenDataStore(context)

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

            // Guardar el token en DataStore
            tokenDataStore.guardarToken(response.token)

            // Retornar el token
            response.token

        } catch (e: Exception) {
            // Si hay error, imprimir en consola y retornar null
            println("Error en login: ${e.message}")
            null
        }
    }

    /**
     * Obtiene el token guardado
     */
    suspend fun obtenerToken(): String? {
        return tokenDataStore.obtenerToken()
    }

    /**
     * Cierra la sesión (limpia el token)
     */
    suspend fun cerrarSesion() {
        tokenDataStore.cerrarSesion()
    }
}