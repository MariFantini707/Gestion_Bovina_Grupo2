package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.model.getGeneroLegible
import com.example.gestion_bovina_grupo2.data.remote.RetrofitInstance
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import com.example.gestion_bovina_grupo2.data.model.VacaResponse

/**
 * Repositorio que maneja las operaciones de vacas con la API
 */
class VacaRepository(private val context: Context) {

    private fun obtenerToken(): String? {
        val sharedPreferences = context.getSharedPreferences(
            "gestion_bovina_prefs",
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString("auth_token", null)
    }
    /**
     * Obtiene todas las vacas desde la API
     * @return Lista de vacas si es exitoso, null si hay error
     */
    suspend fun getVacas(): List<VacaApi>? {
        return try {
            // Obtener el token
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println("❌ Error: No hay token disponible")
                return null
            }

            println(" Token obtenido: ${token.take(20)}...")

            // Hacer la petición GET a la API
            val vacas = RetrofitInstance.api.getVacas("Bearer $token")

            // Retornar la lista de vacas
            vacas

        } catch (e: Exception) {
            println(" Error al obtener vacas: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Obtiene todas las vacas desde la API
     * @return Lista de vacas si es exitoso, null si hay error
     */
    suspend fun getVacasDesactivadas(): List<VacaApi>? {
        return try {
            // Obtener el token
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println(" Error: No hay token disponible")
                return null
            }

            println(" Token obtenido: ${token.take(20)}...")

            // Hacer la petición GET a la API
            val vacasInactivas = RetrofitInstance.api.getVacasDesactivadas("Bearer $token")
            vacasInactivas

        } catch (e: Exception) {
            println(" Error al obtener vacas: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Crea una nueva vaca
     * POST /vacas
     * @param vacaRequest Datos de la vaca a crear
     * @return VacaResponse si es exitoso, null si hay error
     */
    suspend fun crearVaca(vacaRequest: VacaRequest): VacaResponse? {
        return try {

            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println(" Error: No hay token disponible")
                return null
            }

            val response = RetrofitInstance.api.crearVaca(
                authorization = "Bearer $token",
                vacaRequest = vacaRequest
            )

            response

        } catch (e: Exception) {
            println("❌ Error al crear vaca: ${e.message}")
            e.printStackTrace()
            null
        }
    }

}