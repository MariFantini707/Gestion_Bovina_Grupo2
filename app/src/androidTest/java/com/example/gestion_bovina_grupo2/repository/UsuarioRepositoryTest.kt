package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.gestion_bovina_grupo2.data.local.TokenDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsuarioRepositoryTest {

    // Obtenemos contexto real
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var repository: UsuarioRepository
    private lateinit var tokenDataStore: TokenDataStore

    @Before
    fun setup() = runTest {  // ✅ CAMBIO: runTest
        // Inicializamos DataStore
        tokenDataStore = TokenDataStore(context)

        // LIMPIEZA TOTAL - Borramos el DataStore
        tokenDataStore.cerrarSesion()

        // Inicializamos el repo
        repository = UsuarioRepository(context)
    }

    @Test
    fun obtenerToken_retorna_null_si_esta_vacio() = runTest {  // ✅ CAMBIO: runTest y suspend
        // Al inicio no hay nada, debería ser null
        val token = repository.obtenerToken()

        assertNull(token)
    }

    @Test
    fun obtenerToken_retorna_el_valor_correcto_si_ya_existe_sesion() = runTest {  // ✅ CAMBIO: runTest
        // SIMULAMOS UN LOGIN EXITOSO
        // Guardamos un token directamente en DataStore
        tokenDataStore.guardarToken("TOKEN_DE_PRUEBA_123")

        // Probamos si el repositorio sabe leerlo
        val token = repository.obtenerToken()

        // Verificación
        assertEquals("TOKEN_DE_PRUEBA_123", token)
    }

    @Test
    fun cerrarSesion_borra_el_token() = runTest {  // ✅ CAMBIO: runTest
        // Primero guardamos un token (simulando usuario logueado)
        tokenDataStore.guardarToken("TOKEN_A_BORRAR")

        // Ejecutamos la acción de cerrar sesión
        repository.cerrarSesion()

        // Verificamos que se haya borrado
        val token = repository.obtenerToken()
        assertNull(token)
    }

    @Test
    fun guardarToken_guarda_correctamente() = runTest {  // ✅ NUEVO TEST
        // Simulamos un login (guardamos token)
        tokenDataStore.guardarToken("NUEVO_TOKEN_456")

        // Verificamos que se guardó
        val token = repository.obtenerToken()
        assertEquals("NUEVO_TOKEN_456", token)
    }
}