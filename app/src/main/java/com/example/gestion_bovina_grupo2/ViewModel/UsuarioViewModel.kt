package com.example.gestion_bovina_grupo2.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gestion_bovina_grupo2.model.Usuario
import com.example.gestion_bovina_grupo2.model.UsuarioErrores
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(Usuario())

    val estado: StateFlow<Usuario> = _estado

    fun onEmailChange(valor: String){
        _estado.update { it.copy(email = valor, errores = it.errores.copy(email = null)) }
    }

    fun onPasswordChange(valor: String){
        _estado.update { it.copy(password = valor, errores = it.errores.copy(password = null)) }
    }

    //validacion del formulario LOGIN

    fun validarLogin(): Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            email= if(estadoActual.email.isBlank()) "Campo obligatorio" else null,
            password = if(estadoActual.password.length < 8) "La contraseña debe tener 8 caracteres" else null
        )

        val hayErrores = listOfNotNull(
            errores.email,
            errores.password
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }


}