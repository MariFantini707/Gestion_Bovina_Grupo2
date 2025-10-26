package com.example.gestion_bovina_grupo2.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gestion_bovina_grupo2.model.Vaca
import com.example.gestion_bovina_grupo2.model.VacaErrores
import kotlinx.coroutines.flow.update

class VacaViewModel : ViewModel() {

    private val _estado = MutableStateFlow(Vaca(
        diio = TODO(),
        fecha = TODO(),
        genero = TODO(),
        raza = TODO(),
        ubicacion = TODO(),
        enfermedades = TODO(),
        fotoVaca = TODO(),
        erroresVaca = TODO()
    ))
    val estado: StateFlow<Vaca> = _estado

    // Cambiar valores de los campos
    fun onCodigoChange(valor: Int) {
        _estado.update {
            it.copy(
                diio = valor,
                erroresVaca = it.erroresVaca.copy(diio = null)
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

    // Validación de formulario de vaca
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value

        // Primero valida que los campos no estén vacíos
        val erroresVaca = VacaErrores(
            diio = when {
                estadoActual.diio == null -> "El código es obligatorio"
                estadoActual.diio.toString().isBlank() -> "El código debe ser un número válido"
                else -> null
            } as Int?,
            fecha = when {
                estadoActual.fecha.isBlank() -> "La fecha es obligatoria"
                else -> null
            },
            genero = when {
                estadoActual.genero.isBlank() -> "El género es obligatorio"
                else -> null
            },
            raza = when {
                estadoActual.raza.isBlank() -> "La raza es obligatoria"
                else -> null
            },
            ubicacion = when {
                estadoActual.ubicacion.isBlank() -> "La ubicación es obligatoria"
                else -> null
            },
            enfermedades = when {
                estadoActual.enfermedades.isNullOrBlank() -> null
                else -> null
            },
            fotoVaca = when {
                estadoActual.fotoVaca.isNullOrBlank() -> null
                else -> null
            }
        )

        // Si hay errores de campos vacíos, retorna false
        val hayErroresCampos = listOfNotNull(
            erroresVaca.diio,
            erroresVaca.fecha,
            erroresVaca.genero,
            erroresVaca.raza,
            erroresVaca.ubicacion
        ).isNotEmpty()

        if (hayErroresCampos) {
            _estado.update { it.copy(erroresVaca = erroresVaca) }
            return false
        }
        return true
    }



    // Limpiar formulario
    fun limpiarFormulario() {
        _estado.value = Vaca()
    }
}
