package com.example.gestion_bovina_grupo2.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gestion_bovina_grupo2.model.Usuario
import com.example.gestion_bovina_grupo2.model.UsuarioErrores
import kotlinx.coroutines.flow.update


private const val EMAIL_VALIDO = "admin@mail.com"
private const val PASSWORD_VALIDA = "123456"

class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(Usuario())
    val estado: StateFlow<Usuario> = _estado

    fun onEmailChange(valor: String) {
        _estado.update {
            it.copy(
                email = valor,
                errores = it.errores.copy(email = null, loginGeneral = null)
            )
        }
    }

    fun onPasswordChange(valor: String) {
        _estado.update {
            it.copy(
                password = valor,
                errores = it.errores.copy(password = null, loginGeneral = null)
            )
        }
    }

    // Validación del formulario LOGIN
    fun validarLogin(): Boolean {
        val estadoActual = _estado.value

        // Primero valida que los campos no estén vacíos
        val errores = UsuarioErrores(
            email = when {
                estadoActual.email.isBlank() -> "El correo es obligatorio"
                else -> null
            },
            password = when {
                estadoActual.password.isBlank() -> "La contraseña es obligatoria"
                else -> null
            }
        )

        // Si hay errores de campos vacíos, retorna false
        val hayErroresCampos = listOfNotNull(
            errores.email,
            errores.password
        ).isNotEmpty()

        if (hayErroresCampos) {
            _estado.update { it.copy(errores = errores) }
            return false
        }

        // Ahora valida las credenciales
        val credencialesCorrectas = estadoActual.email == EMAIL_VALIDO &&
                estadoActual.password == PASSWORD_VALIDA

        if (!credencialesCorrectas) {
            val erroresLogin = errores.copy(
                loginGeneral = "Correo o contraseña incorrectos"
            )
            _estado.update { it.copy(errores = erroresLogin) }
            return false
        }

        // Todo correcto
        return true
    }

    fun limpiarFormulario() {
        _estado.value = Usuario()
    }
}