package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import com.example.gestion_bovina_grupo2.repository.VacaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel COMPLETO para manejar:
 * - Estado del formulario
 * - Validaciones
 * - Operaciones API (GET, POST, PUT, DELETE)
 */
class VacaApiViewModel(context: Context) : ViewModel() {

    private val repository = VacaRepository(context)

    // ========== ESTADO DEL FORMULARIO ==========
    private val _diio = MutableStateFlow("")
    val diio: StateFlow<String> = _diio

    private val _genero = MutableStateFlow("")
    val genero: StateFlow<String> = _genero

    private val _raza = MutableStateFlow("")
    val raza: StateFlow<String> = _raza

    private val _ubicacion = MutableStateFlow("")
    val ubicacion: StateFlow<String> = _ubicacion

    private val _enfermedades = MutableStateFlow("")
    val enfermedades: StateFlow<String> = _enfermedades

    // ========== ERRORES DE VALIDACIÓN ==========
    private val _diioError = MutableStateFlow<String?>(null)
    val diioError: StateFlow<String?> = _diioError

    private val _generoError = MutableStateFlow<String?>(null)
    val generoError: StateFlow<String?> = _generoError

    private val _razaError = MutableStateFlow<String?>(null)
    val razaError: StateFlow<String?> = _razaError

    private val _ubicacionError = MutableStateFlow<String?>(null)
    val ubicacionError: StateFlow<String?> = _ubicacionError

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> = _updateError

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _deleteSucces = MutableStateFlow(false)
    val deleteSucces: StateFlow<Boolean> = _deleteSucces

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError




    // ========== ESTADOS DE API ==========
    private val _vacas = MutableStateFlow<List<VacaApi>>(emptyList())
    val vacas: StateFlow<List<VacaApi>> = _vacas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating

    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess

    private val _createError = MutableStateFlow<String?>(null)
    val createError: StateFlow<String?> = _createError

    // ========== SETTERS DEL FORMULARIO ==========
    fun onDiioChange(value: String) {
        _diio.value = value
        _diioError.value = null
    }

    fun onGeneroChange(value: String) {
        _genero.value = value
        _generoError.value = null
    }

    fun onRazaChange(value: String) {
        _raza.value = value
        _razaError.value = null
    }

    fun onUbicacionChange(value: String) {
        _ubicacion.value = value
        _ubicacionError.value = null
    }

    fun onEnfermedadesChange(value: String) {
        if (value.length <= 150) {
            _enfermedades.value = value
        }
    }

    // ========== VALIDACIONES ==========
    fun validarFormulario(): Boolean {
        var isValid = true

        // Validar DIIO
        if (_diio.value.trim().isEmpty()) {
            _diioError.value = "Ingrese el DIIO ⚠️"
            isValid = false
        } else if (!_diio.value.all { it.isDigit() }) {
            _diioError.value = "Ingrese solo números ⚠️"
            isValid = false
        }

        // Validar género
        if (_genero.value.isEmpty() || !listOf("m", "h").contains(_genero.value)) {
            _generoError.value = "Seleccione el género ⚠️"
            isValid = false
        }

        // Validar raza
        if (_raza.value.trim().isEmpty()) {
            _razaError.value = "Ingrese la raza ⚠️"
            isValid = false
        }

        // Validar ubicación
        if (_ubicacion.value.trim().isEmpty()) {
            _ubicacionError.value = "Ingrese la ubicación ⚠️"
            isValid = false
        }

        return isValid
    }

    // ========== LIMPIAR FORMULARIO ==========
    fun limpiarFormulario() {
        _diio.value = ""
        _genero.value = ""
        _raza.value = ""
        _ubicacion.value = ""
        _enfermedades.value = ""
        _diioError.value = null
        _generoError.value = null
        _razaError.value = null
        _ubicacionError.value = null
    }

    // ========== OPERACIONES API ==========

    /**
     * GET /vacas (activas)
     */
    fun obtenerVacasActivas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                println("🚀 Obteniendo vacas ACTIVAS...")

                val vacasObtenidas = repository.getVacas()

                _isLoading.value = false

                if (vacasObtenidas != null) {
                    _vacas.value = vacasObtenidas
                    println("✅ ${vacasObtenidas.size} vacas activas cargadas")
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
                    println("✅ ${vacasObtenidas.size} vacas desactivadas cargadas")
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

    /**
     * POST /vacas (crear)
     */
    fun crearVaca(vacaRequest: VacaRequest) {
        viewModelScope.launch {
            try {
                _isCreating.value = true
                _createSuccess.value = false
                _createError.value = null

                println("🚀 Creando nueva vaca...")

                val response = repository.crearVaca(vacaRequest)

                _isCreating.value = false

                if (response != null) {
                    _createSuccess.value = true
                    println("✅ Vaca creada con ID: ${response.id}")

                    // Limpiar formulario
                    limpiarFormulario()

                    // Recargar vacas activas
                    obtenerVacasActivas()
                } else {
                    _createError.value = "Error al crear la vaca"
                    println("❌ No se pudo crear la vaca")
                }

            } catch (e: Exception) {
                _isCreating.value = false
                _createError.value = "Error: ${e.message}"
                println("❌ Error al crear vaca: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * PATCH /vacas/{id} (editar)
     */
    fun cargarVacaParaEditar(vaca: VacaApi) {
        _diio.value = vaca.diio.toString()
        _genero.value = when(vaca.genre) {
            "M" -> "m"  // Backend M/F → Formulario m/h
            "F" -> "h"
            else -> ""
        }
        _raza.value = vaca.race
        _ubicacion.value = vaca.location
        _enfermedades.value = vaca.sick ?: ""
    }

    /**
     * PATCH /vacas/{id} (editar)
     */
    fun editarVaca(id: String, vacaRequest: VacaRequest) {
        viewModelScope.launch {
            try {
                _isUpdating.value = true
                _updateSuccess.value = false
                _updateError.value = null

                val response = repository.editarVaca(id, vacaRequest)

                _isUpdating.value = false

                if (response != null) {
                    _updateSuccess.value = true
                    limpiarFormulario()
                    obtenerVacasActivas()
                } else {
                    _updateError.value = "Error al editar la vaca"
                }
            } catch (e: Exception) {
                _isUpdating.value = false
                _updateError.value = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }
    /**
     * DELETE /vacas/{id} (eliminar)
     */
    fun eliminarVaca(id: String) {
        viewModelScope.launch {
            try {
                _isDeleting.value = true
                _deleteSucces.value = false
                _deleteError.value = null

                val response = repository.eliminarVaca(id)

                _isDeleting.value = false

                if(response == null){
                    _deleteSucces.value = true
                    obtenerVacasActivas()
                } else {
                    _deleteError.value = "Error al eliminar la vaca"
                }

            } catch (e: Exception){
                _isDeleting.value = false
                _deleteError.value = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    /**
     * Resetea los estados de eliminación
     */
    fun resetDeleteStates() {
        _deleteSucces.value = false
        _deleteError.value = null
    }

    /**
     * Resetea los estados de creación
     */
    fun resetCreateStates() {
        _createSuccess.value = false
        _createError.value = null
    }
    /**
     * Resetea los estados de edición
     */
    fun resetUpdateStates() {
        _updateSuccess.value = false
        _updateError.value = null
    }
}