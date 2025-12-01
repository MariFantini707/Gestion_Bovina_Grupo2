package com.example.gestion_bovina_grupo2.ViewModel

import com.example.gestion_bovina_grupo2.repository.ContadorRepository
import com.example.gestion_bovina_grupo2.repository.Contadores
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class VacaViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    // Mockeamos el repositorio completo.
    // No necesitamos Contexto real porque el mock reemplaza toda la lógica interna.
    @MockK
    lateinit var repo: ContadorRepository

    // No declaramos el viewModel con @Inject o lateinit aquí arriba porque necesitamos iniciarlo DENTRO de cada test para controlar el bloque init{}.

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga correctamente los contadores desde el repositorio`() = runTest {
        // Definimos los datos que queremos que devuelva el "repo falso"
        val datosPrueba = Contadores(total = 50, hoy = 15)

        // Definimos el comportamiento ANTES de crear el ViewModel
        coEvery { repo.getCountersOnce() } returns datosPrueba

        // Al crear la instancia, se ejecuta automáticamente el bloque init {}
        val viewModel = VacaViewModel(repo)

        // Dejamos que la corrutina del init termine su trabajo
        advanceUntilIdle()

        // VERIFICACIÓN (ASSERT)
        // Verificamos que el ViewModel llamó al repositorio
        coVerify(exactly = 1) { repo.getCountersOnce() }

        // Verificamos que el valor público 'registradasHoy' sea 15
        assertEquals(15, viewModel.registradasHoy.value)
        assertEquals(50, viewModel.totalVacas.value)
    }

    @Test
    fun `init maneja valores por defecto si el repo devuelve ceros`() = runTest {
        // Caso donde apenas se instaló la app
        val datosIniciales = Contadores(total = 23, hoy = 0) // Tus defaults del repo

        coEvery { repo.getCountersOnce() } returns datosIniciales

        val viewModel = VacaViewModel(repo)
        advanceUntilIdle()

        assertEquals(0, viewModel.registradasHoy.value)
    }
}