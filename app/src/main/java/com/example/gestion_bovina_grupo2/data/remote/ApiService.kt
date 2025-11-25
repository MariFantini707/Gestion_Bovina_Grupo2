package com.example.gestion_bovina_grupo2.data.remote

import com.example.gestion_bovina_grupo2.data.model.LoginRequest
import com.example.gestion_bovina_grupo2.data.model.LoginResponse
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import com.example.gestion_bovina_grupo2.data.model.VacaResponse
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.Path

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
        @Header("Authorization") authorization: String
    ): List<VacaApi>

    /**
     * Define una solicitud GET al endpoint /vacas/desactivadas
     * @return Json con datos de las vacas desactivadas
     */
    @GET("vacas/desactivadas")
    suspend fun getVacasDesactivadas(
        @Header("Authorization") authorization: String
    ): List<VacaApi>

    /**
     * CREAR VACA
     * POST /vacas
     * @Body payload/cuerpo de la vaca para la creacion
     * @return Json con datos de la vaca creada
     */
    @POST("vacas")
    suspend fun crearVaca(
        @Header("Authorization") authorization: String,
        @Body vacaRequest: VacaRequest
    ): VacaResponse

    /**
     * ACTUALIZAR VACA
     * PATCH /vacas
     * @param id de la vaca
     * @Body payload/cuerpo de la vaca para la actualizacion
     * @return Json con datos de la vaca actualizada
     */
    @PATCH("vacas/{id}")
    suspend fun updateVaca(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
        @Body vacaRequest: VacaRequest
    ): VacaResponse

    /**
     * ELIMINAR VACA
     * DELETE /vacas
     * @param id de la vaca
     * @return Json con datos de la vaca eliminada
     */
    @DELETE("vacas/{id}")
    suspend fun deleteVaca(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): VacaResponse

}