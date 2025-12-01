package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestion_bovina_grupo2.model.Usuario
import com.example.gestion_bovina_grupo2.model.UsuarioErrores
import com.example.gestion_bovina_grupo2.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(context: Context) : ViewModel() {

    // Repositorio
    private val repository = UsuarioRepository(context)

    // Estado del formulario
    private val _estado = MutableStateFlow(Usuario())
    val estado: StateFlow<Usuario> = _estado

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de autenticación
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    // Verificar sesión al iniciar
    init {
        viewModelScope.launch {
            val token = repository.obtenerToken()
            _isAuthenticated.value = !token.isNullOrEmpty()
            println("INIT - Token encontrado: ${token?.take(20) ?: "NO HAY TOKEN"}")
            println("INIT - isAuthenticated: ${_isAuthenticated.value}")
        }
    }

    // ========== FUNCIONES PARA ACTUALIZAR CAMPOS ==========

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

    // ========== FUNCIÓN PARA VALIDAR Y HACER LOGIN ==========

    /**
     * Función que obtiene los datos en segundo plano
     * usando viewModelScope.launch (corrutina)
     */
    fun validarYLogin(onSuccess: () -> Unit) {
        val estadoActual = _estado.value

        // Validar campos vacíos
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

        // Si hay errores, mostrarlos y no continuar
        if (errores.email != null || errores.password != null) {
            _estado.update { it.copy(errores = errores) }
            return
        }

        // Si no hay errores, hacer login con la API
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Llamar al repositorio
                val token = repository.login(
                    email = estadoActual.email,
                    password = estadoActual.password
                )

                _isLoading.value = false

                if (token != null) {
                    // Login exitoso
                    println("✅ Login exitoso. Token: $token")
                    _isAuthenticated.value = true
                    limpiarFormulario()
                    onSuccess()
                } else {
                    // Error en el login
                    _estado.update {
                        it.copy(
                            errores = it.errores.copy(
                                loginGeneral = "Correo o contraseña incorrectos"
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                _isLoading.value = false
                println("❌ Error al obtener datos: ${e.localizedMessage}")
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            loginGeneral = "Error de conexión con el servidor"
                        )
                    )
                }
            }
        }
    }

    // ========== FUNCIÓN PARA CERRAR SESIÓN ==========

    /**
     * Cierra la sesión del usuario
     * Limpia el token de SharedPreferences
     * y resetea el formulario
     */
    fun logout() {
        viewModelScope.launch {
            repository.cerrarSesion()
            _isAuthenticated.value = false
            limpiarFormulario()
        }
    }

    /**
     * Limpia el formulario
     */
    fun limpiarFormulario() {
        _estado.value = Usuario()
    }
}