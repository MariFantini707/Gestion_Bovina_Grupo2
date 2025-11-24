package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.model.getGeneroLegible
import com.example.gestion_bovina_grupo2.data.remote.RetrofitInstance

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
            println("🔄 Haciendo petición GET a /vacas...")

            // Obtener el token
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println("❌ Error: No hay token disponible")
                return null
            }

            println("🔑 Token obtenido: ${token.take(20)}...")

            // Hacer la petición GET a la API
            val vacas = RetrofitInstance.api.getVacas("Bearer $token")
            println("✅ GET Vacas exitoso. Total: ${vacas.size} vacas")
            println("═══════════════════════════════════════════════════")

            // Imprimir cada vaca en consola para verificar
            vacas.forEachIndexed { index, vaca ->
                println("📋 Vaca ${index + 1}:")
                println("   ID: ${vaca.id}")
                println("   DIIO: ${vaca.diio}")
                println("   Raza: ${vaca.race}")
                println("   Género: ${vaca.getGeneroLegible()}")
                println("   Fecha Nacimiento: ${vaca.dateBirthday.substring(0, 10)}")
                println("   Ubicación: ${vaca.location}")
                println("   Enfermedad: ${vaca.sick ?: "Ninguna"}")
                println("   ───────────────────────────────────────────────")
            }

            println("═══════════════════════════════════════════════════")
            println("📊 Resumen:")
            println("   Total: ${vacas.size} vacas")
            println("   Hembras: ${vacas.count { it.genre == "F" }} vacas")
            println("   Machos: ${vacas.count { it.genre == "M" }} vacas")
            println("═══════════════════════════════════════════════════")

            // Retornar la lista de vacas
            vacas

        } catch (e: Exception) {
            println("❌ Error al obtener vacas: ${e.message}")
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
            println("🔄 Haciendo petición GET a /vacas...")

            // Obtener el token
            val token = obtenerToken()

            if (token.isNullOrEmpty()) {
                println("❌ Error: No hay token disponible")
                return null
            }

            println("🔑 Token obtenido: ${token.take(20)}...")

            // Hacer la petición GET a la API
            val vacasInactivas = RetrofitInstance.api.getVacasDesactivadas("Bearer $token")
            println("✅ GET Vacas exitoso. Total: ${vacasInactivas.size} vacas")
            println("═══════════════════════════════════════════════════")

            // Imprimir cada vaca en consola para verificar
            vacasInactivas.forEachIndexed { index, vaca ->
                println("📋 Vaca ${index + 1}:")
                println("   ID: ${vaca.id}")
                println("   DIIO: ${vaca.diio}")
                println("   Raza: ${vaca.race}")
                println("   Género: ${vaca.getGeneroLegible()}")
                println("   Fecha Nacimiento: ${vaca.dateBirthday.substring(0, 10)}")
                println("   Ubicación: ${vaca.location}")
                println("   Enfermedad: ${vaca.sick ?: "Ninguna"}")
                println("   ───────────────────────────────────────────────")
            }

            println("═══════════════════════════════════════════════════")
            println("📊 Resumen:")
            println("   Total: ${vacasInactivas.size} vacas")
            println("   Hembras: ${vacasInactivas.count { it.genre == "F" }} vacas")
            println("   Machos: ${vacasInactivas.count { it.genre == "M" }} vacas")
            println("═══════════════════════════════════════════════════")

            // Retornar la lista de vacas
            vacasInactivas

        } catch (e: Exception) {
            println("❌ Error al obtener vacas: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}