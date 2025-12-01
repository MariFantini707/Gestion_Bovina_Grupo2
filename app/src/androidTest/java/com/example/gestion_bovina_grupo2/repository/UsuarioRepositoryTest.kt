package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsuarioRepositoryTest {

    // Obtenemos contexto real
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var repository: UsuarioRepository

    @Before
    fun setup() {
        //LIMPIEZA TOTAL
        // Borramos las SharedPreferences para que cada test empiece limpio
        val prefs = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()

        //Inicializamos el repo
        repository = UsuarioRepository(context)
    }

    @Test
    fun estaAutenticado_retorna_false_si_esta_vacio() {
        // Al inicio no hay nada, debería ser false
        val estaAuth = repository.estaAutenticado()
        val token = repository.obtenerToken()

        assertFalse(estaAuth)
        assertNull(token)
    }

    @Test
    fun obtenerToken_retorna_el_valor_correcto_si_ya_existe_sesion() {
        // SIMULAMOS UN LOGIN EXITOSO (Hackeamos las SharedPreferences)
        // Como 'guardarToken' es privada y 'login' usa internet,
        // escribimos directamente en el archivo de preferencias para simular que ya nos logueamos.
        val prefs = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("auth_token", "TOKEN_DE_PRUEBA_123")
            putBoolean("is_authenticated", true)
            commit()
        }

        // Probamos si el repositorio sabe leerlo
        val token = repository.obtenerToken()
        val estaAuth = repository.estaAutenticado()

        // Verificaciones
        assertEquals("TOKEN_DE_PRUEBA_123", token)
        assertTrue(estaAuth)
    }

    @Test
    fun cerrarSesion_borra_el_token_y_el_estado() {
        // M: HAY que ponernos como si fuera un Usuario logueado
        val prefs = context.getSharedPreferences("gestion_bovina_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("auth_token", "TOKEN_A_BORRAR")
            putBoolean("is_authenticated", true)
            commit()
        }


        repository.cerrarSesion() //Ejecutamos la acción de cerrar sesion

        // con lo siguiente verificamos que se haya borrado todo
        assertNull(repository.obtenerToken())
        assertFalse(repository.estaAutenticado())
    }
}