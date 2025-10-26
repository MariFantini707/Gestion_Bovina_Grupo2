package com.example.gestion_bovina_grupo2.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gestion_bovina_grupo2.model.Vaca
import com.example.gestion_bovina_grupo2.model.VacaErrores
import kotlinx.coroutines.flow.update

class VacaViewModel : ViewModel() {

    private val _estado = MutableStateFlow(Vaca())
    val estado: StateFlow<Vaca> = _estado

    fun onDiioChange(valor: String) {
        _estado.update {
            it.copy(
                diio = valor,
                erroresVaca = it.erroresVaca.copy(diio = null)
            )
        }
    }

    fun onFechaChange(valor: String) {
        _estado.update {
            it.copy(
                fecha = valor,
                erroresVaca = it.erroresVaca.copy(fecha = null)
            )
        }
    }

    fun onGeneroChange(valor: String) {
        _estado.update {
            it.copy(
                genero = valor,
                erroresVaca = it.erroresVaca.copy(genero = null)
            )
        }
    }

    fun onRazaChange(valor: String) {
        _estado.update {
            it.copy(
                raza = valor,
                erroresVaca = it.erroresVaca.copy(raza = null)
            )
        }
    }

    fun onUbicacionChange(valor: String) {
        _estado.update {
            it.copy(
                ubicacion = valor,
                erroresVaca = it.erroresVaca.copy(ubicacion = null)
            )
        }
    }

    fun onEnfermedadesChange(valor: String) {
        if (valor.length <= 150) {
            _estado.update {
                it.copy(
                    enfermedades = valor,
                    erroresVaca = it.erroresVaca.copy(enfermedades = null)
                )
            }
        }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        val erroresVaca = VacaErrores(
            diio = when {
                estadoActual.diio.trim().isEmpty() -> "Ingrese el DIIO! ⚠️"
                !estadoActual.diio.all { it.isDigit() } -> "Ingrese solo números! ⚠️"
                else -> null
            },
            fecha = when {
                estadoActual.fecha.trim().isEmpty() -> "Ingrese una fecha! ⚠️"
                else -> null
            },
            genero = when {
                estadoActual.genero.isEmpty() ||
                        !listOf("m", "h").contains(estadoActual.genero) -> "Ingrese el género ⚠️"
                else -> null
            },
            raza = when {
                estadoActual.raza.trim().isEmpty() -> "Ingrese la raza! ⚠️"
                else -> null
            },
            ubicacion = when {
                estadoActual.ubicacion.trim().isEmpty() -> "Ingrese una ubicación! ⚠️"
                else -> null
            }
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

    fun limpiarFormulario() {
        _estado.value = Vaca()
    }
}