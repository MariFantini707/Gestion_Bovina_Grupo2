package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import com.example.gestion_bovina_grupo2.data.local.TokenDataStore
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.remote.RetrofitInstance
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import com.example.gestion_bovina_grupo2.data.model.VacaResponse

/**
 * Repositorio que maneja las operaciones de vacas con la API
 */
class VacaRepository(private val context: Context) {

    private val tokenDataStore = TokenDataStore(context)

    private suspend fun obtenerToken(): String? {
        return tokenDataStore.obtenerToken()
    }

    /**
     * Obtiene todas las vacas activas desde la API
     * @return Lista de vacas si es exitoso, null si hay error
     */
    suspend fun getVacas(): List<VacaApi>? {
        return try {
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println("❌ Error: No hay token disponible")
                return null
            }

            println("🔑 Token obtenido: ${token.take(20)}...")

            val vacas = RetrofitInstance.api.getVacas("Bearer $token")
            vacas

        } catch (e: Exception) {
            println("❌ Error al obtener vacas: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Obtiene todas las vacas desactivadas desde la API
     * @return Lista de vacas si es exitoso, null si hay error
     */
    suspend fun getVacasDesactivadas(): List<VacaApi>? {
        return try {
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println("❌ Error: No hay token disponible")
                return null
            }

            println("🔑 Token obtenido: ${token.take(20)}...")

            val vacasInactivas = RetrofitInstance.api.getVacasDesactivadas("Bearer $token")
            vacasInactivas

        } catch (e: Exception) {
            println("❌ Error al obtener vacas desactivadas: ${e.message}")
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
                println("❌ Error: No hay token disponible")
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

    /**
     * Editar una vaca
     * PATCH /vacas/{id}
     * @param id ID de la vaca a editar
     * @param vacaRequest Datos de la vaca a editar
     * @return VacaResponse si es exitoso, null si hay error
     */
    suspend fun editarVaca(id: String, vacaRequest: VacaRequest): VacaResponse? {
        return try {
            val token = obtenerToken()

            if (token == null) {
                println("❌ No hay token disponible")
                return null
            }

            println("📝 Haciendo petición PATCH a /vacas/$id...")
            val response = RetrofitInstance.api.updateVaca(
                id = id,
                authorization = "Bearer $token",
                vacaRequest = vacaRequest
            )
            println("✅ PATCH Vaca exitoso! ID: ${response.id}")
            response

        } catch (e: Exception) {
            println("❌ Error en Repository.editarVaca(): ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Eliminar una vaca
     * DELETE /vacas/{id}
     * @param id ID de la vaca a eliminar
     * @return VacaResponse si es exitoso, null si hay error
     */
    suspend fun eliminarVaca(id: String): VacaResponse? {
        return try {
            val token = obtenerToken()

            if (token == null) {
                println("❌ No hay token disponible")
                return null
            }

            println("🗑️ Haciendo petición DELETE a /vacas/$id...")
            val response = RetrofitInstance.api.deleteVaca(
                id = id,
                authorization = "Bearer $token"
            )
            println("✅ DELETE Vaca exitoso! ID: ${response.id}")
            response

        } catch (e: Exception){
            println("❌ Error en Repository.eliminarVaca(): ${e.message}")
            e.printStackTrace()
            null
        }
    }
}