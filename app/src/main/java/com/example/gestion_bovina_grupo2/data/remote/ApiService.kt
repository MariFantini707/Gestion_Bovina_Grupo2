package com.example.gestion_bovina_grupo2.data.remote

import com.example.gestion_bovina_grupo2.data.model.LoginRequest
import com.example.gestion_bovina_grupo2.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Esta interfaz define los endpoints HTTP
 * Retrofit genera automáticamente la implementación
 */
interface ApiService {

    /**
     * Define una solicitud POST al endpoint /login
     * @param loginRequest Credenciales del usuario
     * @return LoginResponse con el token
     */
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}