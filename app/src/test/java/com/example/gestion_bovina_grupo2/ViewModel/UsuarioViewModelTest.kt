package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import com.example.gestion_bovina_grupo2.model.Usuario
import com.example.gestion_bovina_grupo2.model.UsuarioErrores
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
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)

        // Interceptamos el constructor del repositorio
        mockkConstructor(UsuarioRepository::class)

        // Configuramos comportamientos por defecto para evitar NullPointerExceptions
        coEvery { anyConstructed<UsuarioRepository>().login(any(), any()) } returns "fake_token_123"
        every { anyConstructed<UsuarioRepository>().cerrarSesion() } just Runs

        viewModel = UsuarioViewModel(context)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // ACTUALIZACIÓN DE CAMPOS
    @Test
    fun `onEmailChange actualiza el email y limpia errores`() {
        // Simulamos escribir en el campo email
        viewModel.onEmailChange("test@correo.com")

        // Verificamos que el estado cambio
        assertEquals("test@correo.com", viewModel.estado.value.email)
        // Verificamos que el error de email se limpio (es null)
        assertNull(viewModel.estado.value.errores.email)
    }

    @Test
    fun `onPasswordChange actualiza el password`() {
        viewModel.onPasswordChange("123456")
        assertEquals("123456", viewModel.estado.value.password)
    }

    // VALIDACIONES LOCALES
    @Test
    fun `validarYLogin muestra errores si los campos están vacios`() = runTest {
        // Como el Usuario por defecto tiene strings vacíos (""), no necesitamos setear nada

        var successLlamado = false

        // Ejecutamos validacion
        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        // Verificamos que NO tuvo éxito
        assertFalse(successLlamado)

        // Verificamos los mensajes de error exactos que ya habiamos deifinido en el ViewModel
        assertEquals("El correo es obligatorio", viewModel.estado.value.errores.email)
        assertEquals("La contraseña es obligatoria", viewModel.estado.value.errores.password)

        // Verificamos que NO se llamó al repositorio (ahorro de recursos)
        coVerify(exactly = 0) { anyConstructed<UsuarioRepository>().login(any(), any()) }
    }

    // LOGIN CON API (CASOS DE ÉXITO Y ERROR)
    @Test
    fun `validarYLogin funciona correctamente (Login Exitoso)`() = runTest {
        //Llenamos el formulario con datos válidos
        viewModel.onEmailChange("usuario@test.com")
        viewModel.onPasswordChange("123456")

        //Mock: El repositorio devuelve un token válido
        coEvery {
            anyConstructed<UsuarioRepository>().login("usuario@test.com", "123456")
        } returns "token_valido_abc"

        var successLlamado = false

        //Ejecutamos
        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        //Verificaciones
        assertTrue(successLlamado, "El callback onSuccess debería haberse ejecutado")
        assertFalse(viewModel.isLoading.value) // La carga debe haber terminado

        // Al tener éxito, el ViewModel llama a limpiarFormulario(),
        // así que el email debería volver a estar vacío.
        assertEquals("", viewModel.estado.value.email)
        assertEquals("", viewModel.estado.value.password)
    }

    @Test
    fun `validarYLogin maneja error de credenciales incorrectas (Login Fallido)`() = runTest {
        viewModel.onEmailChange("fail@test.com")
        viewModel.onPasswordChange("wrong_pass")

        // Mock: El repositorio devuelve null (login fallido)
        coEvery {
            anyConstructed<UsuarioRepository>().login(any(), any())
        } returns null

        var successLlamado = false
        viewModel.validarYLogin(onSuccess = { successLlamado = true })
        advanceUntilIdle()

        // Verificamos que falló
        assertFalse(successLlamado)
        // Verificamos el mensaje de error general
        assertEquals("Correo o contraseña incorrectos", viewModel.estado.value.errores.loginGeneral)
    }

    @Test
    fun `validarYLogin maneja excepciones de red`() = runTest {
        viewModel.onEmailChange("error@test.com")
        viewModel.onPasswordChange("123")

        // Mock: El repositorio lanza una excepción
        coEvery {
            anyConstructed<UsuarioRepository>().login(any(), any())
        } throws Exception("Error 500")

        viewModel.validarYLogin(onSuccess = {})
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertEquals("Error de conexión con el servidor", viewModel.estado.value.errores.loginGeneral)
    }

    // LOGOUT Y LIMPIEZA
    @Test
    fun `logout cierra sesion y limpia formulario`() {
        // Llenamos el formulario para ver si se limpia
        viewModel.onEmailChange("usuario@mail.com")

        viewModel.logout()

        // Verifica que se llamó a cerrar sesión en el repo
        verify(exactly = 1) { anyConstructed<UsuarioRepository>().cerrarSesion() }

        // Verifica que el formulario volvió a estar vacío
        assertEquals("", viewModel.estado.value.email)

        // como Usuario() tiene valores por defecto, el rut también debería ser "" aunque no lo hayamos tocado.
        assertEquals("", viewModel.estado.value.rut)
    }
}