package com.example.gestion_bovina_grupo2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestion_bovina_grupo2.model.Vaca
import com.example.gestion_bovina_grupo2.model.VacaErrores
import com.example.gestion_bovina_grupo2.repository.ContadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FECHA_REGEX = Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\d{2}$""")

private val SDF = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
    isLenient = false // evita 32/13/2025, etc.
}

private fun parseFechaOrNull(texto: String): Date? = try {
    SDF.parse(texto)
} catch (_: Exception) { null }

class VacaViewModel(
    private val contadorRepo: ContadorRepository
) : ViewModel() {

    // Formulario
    private val _estado = MutableStateFlow(Vaca())
    val estado: StateFlow<Vaca> = _estado

    // Lista en memoria (si quieres también persistirla, lo vemos luego)
    private val _vacas = MutableStateFlow<List<Vaca>>(emptyList())

    // Contadores (persistentes)
    private val _totalVacas = MutableStateFlow(0)
    val totalVacas: StateFlow<Int> = _totalVacas

    private val _registradasHoy = MutableStateFlow(0)
    val registradasHoy: StateFlow<Int> = _registradasHoy

    init {
        // Cargar contadores desde DataStore al iniciar el VM
        viewModelScope.launch {
            val c = contadorRepo.getCountersOnce()
            _totalVacas.value = c.total
            _registradasHoy.value = c.hoy
        }
    }

    // ----------------- setters del formulario -----------------
    fun onDiioChange(v: String) = _estado.update { it.copy(diio = v, erroresVaca = it.erroresVaca.copy(diio = null)) }
    fun onFechaChange(v: String) = _estado.update { it.copy(fecha = v, erroresVaca = it.erroresVaca.copy(fecha = null)) }
    fun onGeneroChange(v: String) = _estado.update { it.copy(genero = v, erroresVaca = it.erroresVaca.copy(genero = null)) }
    fun onRazaChange(v: String) = _estado.update { it.copy(raza = v, erroresVaca = it.erroresVaca.copy(raza = null)) }
    fun onUbicacionChange(v: String) = _estado.update { it.copy(ubicacion = v, erroresVaca = it.erroresVaca.copy(ubicacion = null)) }
    fun onEnfermedadesChange(v: String) {
        if (v.length <= 150) _estado.update { it.copy(enfermedades = v, erroresVaca = it.erroresVaca.copy(enfermedades = null)) }
    }

    // ----------------- validación -----------------
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        // --- Validación de fecha (String -> dd/MM/yyyy) ---
        val fechaError: String? = when {
            estadoActual.fecha.trim().isEmpty() ->
                "Ingrese una fecha! ⚠️"

            !FECHA_REGEX.matches(estadoActual.fecha) ->
                "Formato inválido (dd/MM/aaaa) ⚠️"

            parseFechaOrNull(estadoActual.fecha) == null ->
                "Fecha inválida ⚠️"

            // Evitar fechas futuras (opcional, deja o quita esta regla)
            parseFechaOrNull(estadoActual.fecha)!!.after(Date()) ->
                "La fecha no puede ser futura ⚠️"

            else -> null
        }

        val erroresVaca = VacaErrores(
            diio = when {
                estadoActual.diio.trim().isEmpty() -> "Ingrese el DIIO! ⚠️"
                !estadoActual.diio.all { it.isDigit() } -> "Ingrese solo números! ⚠️"
                else -> null
            },
            fecha = fechaError,
            genero = when {
                estadoActual.genero.isEmpty() || !listOf("m", "h").contains(estadoActual.genero) ->
                    "Ingrese el género ⚠️"
                else -> null
            },
            raza = if (estadoActual.raza.trim().isEmpty()) "Ingrese la raza! ⚠️" else null,
            ubicacion = if (estadoActual.ubicacion.trim().isEmpty()) "Ingrese una ubicación! ⚠️" else null
        )

        val hayErrores = listOfNotNull(
            erroresVaca.diio,
            erroresVaca.fecha,
            erroresVaca.genero,
            erroresVaca.raza,
            erroresVaca.ubicacion
        ).isNotEmpty()

        if (hayErrores) {
            _estado.update { it.copy(erroresVaca = erroresVaca) }
            return false
        }
        return true
    }

    // ----------------- crear vaca + persistir contadores -----------------
    fun crearVaca(): Boolean {
        if (!validarFormulario()) return false

        val nueva = _estado.value.copy()
        _vacas.update { it + nueva }

        // Actualiza Y persiste contadores en DataStore
        viewModelScope.launch {
            val c = contadorRepo.incrementOnVacaGuardada()
            _totalVacas.value = c.total
            _registradasHoy.value = c.hoy
        }

        limpiarFormulario()
        return true
    }

    fun limpiarFormulario() { _estado.value = Vaca() }

    // Factory para crear el ViewModel con el repo
    companion object {
        fun provideFactory(repo: ContadorRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return VacaViewModel(repo) as T
                }
            }
    }
}
