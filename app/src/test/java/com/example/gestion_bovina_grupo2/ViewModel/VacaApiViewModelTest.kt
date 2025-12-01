package com.example.gestion_bovina_grupo2.ViewModel

import android.content.Context
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import com.example.gestion_bovina_grupo2.data.model.VacaResponse
import com.example.gestion_bovina_grupo2.repository.VacaRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class VacaApiViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @MockK(relaxed = true)
    lateinit var context: Context

    private lateinit var viewModel: VacaApiViewModel

    // Objeto de prueba para respuestas del servidor (Create/Update/Delete)
    private val vacaResponseMock = VacaResponse(
        id = "1",
        diio = 123,
        dateBirthday = "2024-01-01",
        genre = "M",
        race = "Holstein",
        location = "Corral 1",
        sick = null,
        cowState = true,
        version = 0
    )

    // Objeto de prueba para listas de vacas (Get)
    private val vacaApiMock = VacaApi(
        id = "1",
        diio = 123,
        dateBirthday = "2024-01-01",
        genre = "M",
        race = "Holstein",
        location = "Corral 1",
        sick = null,
        cowState = true
    )

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)

        mockkConstructor(VacaRepository::class)
        // Por defecto, getVacas devuelve lista vacía
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns emptyList()

        viewModel = VacaApiViewModel(context)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    // VALIDACIONES DEL FORMULARIO
    @Test
    fun `validarFormulario falla cuando DIIO está vacío`() {
        viewModel.onDiioChange("")
        viewModel.onGeneroChange("m")
        viewModel.onRazaChange("Holstein")
        viewModel.onUbicacionChange("Corral 1")
        val valido = viewModel.validarFormulario()
        assertFalse(valido)
        assertEquals("Ingrese el DIIO ⚠️", viewModel.diioError.value)
    }

    @Test
    fun `validarFormulario falla cuando DIIO no es numerico`() {
        viewModel.onDiioChange("12AB")
        val valido = viewModel.validarFormulario()
        assertFalse(valido)
        assertEquals("Ingrese solo números ⚠️", viewModel.diioError.value)
    }

    @Test
    fun `validarFormulario es valido cuando todos los campos están correctos`() {
        viewModel.onDiioChange("123")
        viewModel.onGeneroChange("m")
        viewModel.onRazaChange("Holstein")
        viewModel.onUbicacionChange("Corral 1")
        val valido = viewModel.validarFormulario()
        assertTrue(valido)
        assertNull(viewModel.diioError.value)
    }

    @Test
    fun `cargarVacaParaEditar rellena correctamente los campos`() {
        viewModel.cargarVacaParaEditar(vacaApiMock)
        assertEquals("123", viewModel.diio.value)
        assertEquals("m", viewModel.genero.value)
        assertEquals("Holstein", viewModel.raza.value)
    }

    // OBTENER VACAS ACTIVAS
    @Test
    fun `obtenerVacasActivas carga las vacas correctamente`() = runTest {
        val listaVacas = listOf(vacaApiMock)

        // getVacas devuelve List<VacaApi>
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns listaVacas

        viewModel.obtenerVacasActivas()
        advanceUntilIdle()

        assertEquals(1, viewModel.vacas.value.size)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `obtenerVacasActivas genera error cuando repo devuelve null`() = runTest {
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns null

        viewModel.obtenerVacasActivas()
        advanceUntilIdle()

        assertEquals("Error al cargar las vacas", viewModel.error.value)
    }

    // CREAR VACA
    @Test
    fun `crearVaca funciona correctamente cuando repo devuelve respuesta`() = runTest {
        val request = VacaRequest(123, "2024-01-01", "m", "Holstein", "Corral 1", null)

        // crearVaca devuelve VacaResponse (usamos vacaResponseMock)
        coEvery { anyConstructed<VacaRepository>().crearVaca(request) } returns vacaResponseMock

        // Al crear con éxito, el VM recarga la lista (getVacas)
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns listOf(vacaApiMock)

        viewModel.crearVaca(request)
        advanceUntilIdle()

        assertTrue(viewModel.createSuccess.value)
        assertNull(viewModel.createError.value)
    }

    @Test
    fun `crearVaca falla cuando repo devuelve null`() = runTest {
        val request = VacaRequest(123, "2024-01-01", "m", "Holstein", "Corral 1", null)

        // crearVaca devuelve null en caso de error interno/conexión
        coEvery { anyConstructed<VacaRepository>().crearVaca(request) } returns null

        viewModel.crearVaca(request)
        advanceUntilIdle()

        assertFalse(viewModel.createSuccess.value)
        assertEquals("Error al crear la vaca", viewModel.createError.value)
    }

    // EDITAR VACA
    @Test
    fun `editarVaca funciona correctamente cuando repo devuelve respuesta`() = runTest {
        val request = VacaRequest(123, "2024-01-01", "m", "Holstein", "Corral 1", null)

        // editarVaca devuelve VacaResponse
        coEvery { anyConstructed<VacaRepository>().editarVaca("1", request) } returns vacaResponseMock
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns listOf(vacaApiMock)

        viewModel.editarVaca("1", request)
        advanceUntilIdle()

        assertTrue(viewModel.updateSuccess.value)
        assertNull(viewModel.updateError.value)
    }

    @Test
    fun `editarVaca falla cuando repo devuelve null`() = runTest {
        val request = VacaRequest(123, "2024-01-01", "m", "Holstein", "Corral 1", null)

        coEvery { anyConstructed<VacaRepository>().editarVaca("1", request) } returns null

        viewModel.editarVaca("1", request)
        advanceUntilIdle()

        assertFalse(viewModel.updateSuccess.value)
        assertEquals("Error al editar la vaca", viewModel.updateError.value)
    }


    // ELIMINAR VACA
    @Test
    fun `eliminarVaca funciona correctamente cuando repo devuelve null (caso éxito)`() = runTest {
        // Según tu lógica: if(response == null) -> Éxito
        coEvery { anyConstructed<VacaRepository>().eliminarVaca("1") } returns null
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns emptyList()

        viewModel.eliminarVaca("1")
        advanceUntilIdle()

        assertTrue(viewModel.deleteSucces.value)
        assertNull(viewModel.deleteError.value)
    }

    @Test
    fun `eliminarVaca falla cuando repo devuelve respuesta`() = runTest {
        // Según tu lógica: else (si response != null) -> Error
        // Por eso aquí devolvemos un objeto VacaResponse para simular el fallo
        coEvery { anyConstructed<VacaRepository>().eliminarVaca("1") } returns vacaResponseMock

        viewModel.eliminarVaca("1")
        advanceUntilIdle()

        assertFalse(viewModel.deleteSucces.value)
        assertEquals("Error al eliminar la vaca", viewModel.deleteError.value)
    }


    //TESTEAR EXCEPCIONES (CATCH BLOCK)
    @Test
    fun `obtenerVacasActivas maneja excepciones correctamente`() = runTest {
        // Simulamos que el repositorio lanza una excepción (ej. sin internet)
        coEvery { anyConstructed<VacaRepository>().getVacas() } throws Exception("Error de red fatal")

        viewModel.obtenerVacasActivas()
        advanceUntilIdle()

        // Verificamos que el isLoading se apague
        assertFalse(viewModel.isLoading.value)

        // Verificamos que el mensaje de error en el ViewModel sea el esperado
        assertEquals("Error: Error de red fatal", viewModel.error.value)
    }

    //TESTEAR OBTENER VACAS DESACTIVADAS
    @Test
    fun `obtenerVacasDesactivadas carga correctamente la lista`() = runTest {
        // Reutilizamos el mock que que ya teníamos def.
        val listaVacas = listOf(vacaApiMock)

        // Mockeamos la llamada específica a getVacasDesactivadas
        coEvery { anyConstructed<VacaRepository>().getVacasDesactivadas() } returns listaVacas

        viewModel.obtenerVacasDesactivadas()
        advanceUntilIdle()

        assertEquals(1, viewModel.vacas.value.size)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `obtenerVacasDesactivadas maneja error cuando devuelve null`() = runTest {
        coEvery { anyConstructed<VacaRepository>().getVacasDesactivadas() } returns null

        viewModel.obtenerVacasDesactivadas()
        advanceUntilIdle()

        assertEquals("Error al cargar las vacas desactivadas", viewModel.error.value)
    }

    //TESTEAR RESETEO DE ESTADOS (LIMPIAR FORMULARIO)
    @Test
    fun `limpiarFormulario resetea todos los campos y errores`() {
        // "Ensuciamos" el estado poniendo valores
        viewModel.onDiioChange("99999")
        viewModel.onRazaChange("Angus")
        viewModel.onUbicacionChange("Zona Norte")
        // Forzamos un error simulado pero como los _states son privados, probamos limpiando tras setear valores válidos.

        //Ejecutamos la limpieza
        viewModel.limpiarFormulario()

        /*Verificamos que todo esté vacío o nulo*/
        assertEquals("", viewModel.diio.value)
        assertEquals("", viewModel.raza.value)
        assertEquals("", viewModel.ubicacion.value)
        assertEquals("", viewModel.enfermedades.value)

        // Verificamos que los errores también se limpien
        assertNull(viewModel.diioError.value)
        assertNull(viewModel.razaError.value)
    }

    //Reseteo de estados de éxito/error tras eliminar
    @Test
    fun `resetDeleteStates limpia los estados de eliminación`() = runTest {
        //simulamos un estado post-eliminación
        coEvery { anyConstructed<VacaRepository>().eliminarVaca("1") } returns null // Exito
        coEvery { anyConstructed<VacaRepository>().getVacas() } returns emptyList()

        viewModel.eliminarVaca("1")
        advanceUntilIdle()

        // Confirmamos que está en true
        assertTrue(viewModel.deleteSucces.value)

        //Llamamos al reset
        viewModel.resetDeleteStates()

        //Verificamos que volvió a false/null
        assertFalse(viewModel.deleteSucces.value)
        assertNull(viewModel.deleteError.value)
    }
}