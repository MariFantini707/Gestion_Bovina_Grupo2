package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class ContadorRepositoryTest {

    // Obtenemos el contexto real del emulador
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var repository: ContadorRepository

    @Before
    fun setup() {
        /*Antes de cada test, borramos el archivo físico de DataStore.
        Esto asegura que cada test empiece "como si instalaras la app de cero".
        El nombre "vacas_counters" viene de la constante DS_NAME.*/
        val dataStoreFile = File(context.filesDir, "datastore/vacas_counters.preferences_pb")
        if (dataStoreFile.exists()) {
            dataStoreFile.delete()
        }

        repository = ContadorRepository(context)
    }

    @Test
    fun getCountersOnce_retorna_valores_por_defecto_al_inicio() = runTest {
        //Leemos sin haber guardado nada
        val contadores = repository.getCountersOnce()

        //Verificamos que use tus constantes DEFAULT_TOTAL (23) y DEFAULT_HOY (0)
        assertEquals(23, contadores.total)
        assertEquals(0, contadores.hoy)
    }

    @Test
    fun incrementOnVacaGuardada_suma_uno_a_ambos_valores() = runTest {
        // Estado inicial implícito: 23 y 0

        //Ejecutamos la acción de incrementar
        val resultado = repository.incrementOnVacaGuardada()

        //Verificamos el retorno inmediato
        assertEquals(24, resultado.total) // 23 + 1
        assertEquals(1, resultado.hoy)    // 0 + 1

        //Verificamos persistencia (leyendo de nuevo)
        val lecturaPosterior = repository.getCountersOnce()
        assertEquals(24, lecturaPosterior.total)
    }

    @Test
    fun setCounters_sobrescribe_y_guarda_los_datos() = runTest {
        //Guardamos valores arbitrarios
        repository.setCounters(total = 100, hoy = 50)

        //Simulamos "reiniciar el repo" (creamos nueva instancia)
        val nuevoRepo = ContadorRepository(context)
        val lectura = nuevoRepo.getCountersOnce()

        //Verificamos que los datos siguen ahí
        assertEquals(100, lectura.total)
        assertEquals(50, lectura.hoy)
    }
}