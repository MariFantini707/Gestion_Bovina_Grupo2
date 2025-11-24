package com.example.gestion_bovina_grupo2.data.remote

import com.example.gestion_bovina_grupo2.data.model.LoginRequest
import com.example.gestion_bovina_grupo2.data.model.LoginResponse
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    /**
     * Define una solicitud POST al endpoint /login
     * @param loginRequest Credenciales del usuario
     * @return LoginResponse con el token
     */
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    /**
     * Define una solicitud GET al endpoint /vacas
     * @return Json con datos de las vacas activas
     */
    @GET("vacas")
    suspend fun getVacas(
        @Header("Authorization") authorization: String  // ← Agregar
    ): List<VacaApi>

    /**
     * Define una solicitud GET al endpoint /vacas/desactivadas
     * @return Json con datos de las vacas desactivadas
     */
    @GET("vacas/desactivadas")
    suspend fun getVacasDesactivadas(
        @Header("Authorization") authorization: String
    ): List<VacaApi>
}