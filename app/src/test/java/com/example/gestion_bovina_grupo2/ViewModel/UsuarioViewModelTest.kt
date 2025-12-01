package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import com.example.gestion_bovina_grupo2.repository.UsuarioRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK(relaxed = true)
    lateinit var context: Context

    private lateinit var viewModel: UsuarioViewModel

    @BeforeEach
    fun setup() = runTest {
        MockKAnnotations.init(this@UsuarioViewModelTest)
        Dispatchers.setMain(dispatcher)

        // Interceptamos el constructor del repositorio
        mockkConstructor(UsuarioRepository::class)

        // Mock para obtenerToken (usado en el init)
        coEvery { anyConstructed<UsuarioRepository>().obtenerToken() } returns null

        // Configuramos comportamientos por defecto
        coEvery { anyConstructed<UsuarioRepository>().login(any(), any()) } returns "fake_token_123"
        coEvery { anyConstructed<UsuarioRepository>().cerrarSesion() } just Runs  // ✅ CAMBIO: coEvery

        viewModel = UsuarioViewModel(context)

        // Esperar a que el init termine
        advanceUntilIdle()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // ===========================================================
    // NUEVO - TEST DE AUTENTICACIÓN INICIAL
    // ===========================================================

    @Test
    fun `init verifica token y establece isAuthenticated correctamente - sin token`() = runTest {
        // El token es null (configurado en setup)
        assertFalse(viewModel.isAuthenticated.value)
    }

    @Test
    fun `init verifica token y establece isAuthenticated correctamente - con token`() = runTest {
        // Mock: hay un token guardado
        coEvery { anyConstructed<UsuarioRepository>().obtenerToken() } returns "token_guardado"

        val viewModelConToken = UsuarioViewModel(context)
        advanceUntilIdle()

        assertTrue(viewModelConToken.isAuthenticated.value)
    }

    // ===========================================================
    // ACTUALIZACIÓN DE CAMPOS
    // ===========================================================

    @Test
    fun `onEmailChange actualiza el email y limpia errores`() {
        viewModel.onEmailChange("test@correo.com")
        assertEquals("test@correo.com", viewModel.estado.value.email)
        assertNull(viewModel.estado.value.errores.email)
    }

    @Test
    fun `onPasswordChange actualiza el password`() {
        viewModel.onPasswordChange("123456")
        assertEquals("123456", viewModel.estado.value.password)
    }

    // ===========================================================
    // VALIDACIONES LOCALES
    // ===========================================================

    @Test
    fun `validarYLogin muestra errores si los campos están vacios`() = runTest {
        var successLlamado = false

        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        assertFalse(successLlamado)
        assertEquals("El correo es obligatorio", viewModel.estado.value.errores.email)
        assertEquals("La contraseña es obligatoria", viewModel.estado.value.errores.password)

        coVerify(exactly = 0) { anyConstructed<UsuarioRepository>().login(any(), any()) }
    }

    // ===========================================================
    // LOGIN CON API (CASOS DE ÉXITO Y ERROR)
    // ===========================================================

    @Test
    fun `validarYLogin funciona correctamente (Login Exitoso)`() = runTest {
        viewModel.onEmailChange("usuario@test.com")
        viewModel.onPasswordChange("123456")

        coEvery {
            anyConstructed<UsuarioRepository>().login("usuario@test.com", "123456")
        } returns "token_valido_abc"

        var successLlamado = false

        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        assertTrue(successLlamado, "El callback onSuccess debería haberse ejecutado")
        assertFalse(viewModel.isLoading.value)

        // Verificar que isAuthenticated cambió a true
        assertTrue(viewModel.isAuthenticated.value)

        assertEquals("", viewModel.estado.value.email)
        assertEquals("", viewModel.estado.value.password)
    }

    @Test
    fun `validarYLogin maneja error de credenciales incorrectas (Login Fallido)`() = runTest {
        viewModel.onEmailChange("fail@test.com")
        viewModel.onPasswordChange("wrong_pass")

        coEvery {
            anyConstructed<UsuarioRepository>().login(any(), any())
        } returns null

        var successLlamado = false
        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        assertFalse(successLlamado)
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errores.loginGeneral)

        // isAuthenticated debe seguir en false
        assertFalse(viewModel.isAuthenticated.value)
    }

    @Test
    fun `validarYLogin maneja excepciones de red`() = runTest {
        viewModel.onEmailChange("error@test.com")
        viewModel.onPasswordChange("123")

        coEvery {
            anyConstructed<UsuarioRepository>().login(any(), any())
        } throws Exception("Error 500")

        viewModel.validarYLogin(onSuccess = {})
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertEquals("Error de conexión con el servidor", viewModel.estado.value.errores.loginGeneral)
    }

    // ===========================================================
    // LOGOUT Y LIMPIEZA
    // ===========================================================

    @Test
    fun `logout cierra sesion y limpia formulario`() = runTest {  // ✅ CAMBIO: runTest
        // Primero simulamos que está autenticado
        viewModel.onEmailChange("usuario@mail.com")

        viewModel.logout()
        advanceUntilIdle()  // Esperar que termine la coroutine

        // ✅ CAMBIO: coVerify en lugar de verify
        coVerify(exactly = 1) { anyConstructed<UsuarioRepository>().cerrarSesion() }

        // Verificar que isAuthenticated cambió a false
        assertFalse(viewModel.isAuthenticated.value)

        assertEquals("", viewModel.estado.value.email)
        assertEquals("", viewModel.estado.value.rut)
    }

    // ===========================================================
    // NUEVO - TEST DE PERSISTENCIA
    // ===========================================================

    @Test
    fun `login exitoso actualiza isAuthenticated a true`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("pass123")

        coEvery {
            anyConstructed<UsuarioRepository>().login(any(), any())
        } returns "token_abc"

        viewModel.validarYLogin(onSuccess = {})
        advanceUntilIdle()

        assertTrue(viewModel.isAuthenticated.value)
    }

    @Test
    fun `logout actualiza isAuthenticated a false`() = runTest {
        viewModel.logout()
        advanceUntilIdle()

        assertFalse(viewModel.isAuthenticated.value)
    }
}