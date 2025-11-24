package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.repository.VacaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar las operaciones de vacas con la API
 * Versión con token manual - necesita Context
 */
class VacaApiViewModel(context: Context) : ViewModel() {

    // Repositorio con context para obtener el token
    private val repository = VacaRepository(context)

    private val _vacas = MutableStateFlow<List<VacaApi>>(emptyList())
    val vacas: StateFlow<List<VacaApi>> = _vacas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Obtiene vacas ACTIVAS desde la API
     * GET /vacas
     */
    fun obtenerVacasActivas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                println("🚀 Iniciando obtención de vacas...")

                val vacasObtenidas = repository.getVacas()

                _isLoading.value = false

                if (vacasObtenidas != null) {
                    _vacas.value = vacasObtenidas
                    println("✅ ${vacasObtenidas.size} vacas cargadas en el ViewModel")
                } else {
                    _error.value = "Error al cargar las vacas"
                    println("❌ No se pudieron cargar las vacas")
                }

            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Error: ${e.message}"
                println("❌ Error en ViewModel: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Obtiene vacas DESACTIVADAS desde la API
     * GET /vacas/desactivadas
     */
    fun obtenerVacasDesactivadas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                println("🚀 Obteniendo vacas DESACTIVADAS...")

                val vacasObtenidas = repository.getVacasDesactivadas()

                _isLoading.value = false

                if (vacasObtenidas != null) {
                    _vacas.value = vacasObtenidas
                    println("✅ ${vacasObtenidas.size} vacas desactivadas cargadas en el ViewModel")
                } else {
                    _error.value = "Error al cargar las vacas desactivadas"
                    println("❌ No se pudieron cargar las vacas desactivadas")
                }

            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Error: ${e.message}"
                println("❌ Error en ViewModel: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}