package com.example.gestion_bovina_grupo2.ui.theme.composables

import org.junit.Assert.*
import com.example.gestion_bovina_grupo2.ui.theme.composables.VacaCard
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class VacaCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Datos de prueba
    private val vacaSana = VacaApi(
        id = "1",
        diio = 100,
        dateBirthday = "2023-05-20T00:00:00.000Z",
        genre = "M",
        race = "Holstein",
        location = "Campo Norte",
        sick = null, // No tiene enfermedad
        cowState = true // Activa
    )

    private val vacaEnferma = VacaApi(
        id = "2",
        diio = 200,
        dateBirthday = "2022-01-01T00:00:00.000Z",
        genre = "F",
        race = "Angus",
        location = "Enfermería",
        sick = "Mastitis", // Con enfermedad
        cowState = true
    )

    @Test
    fun muestra_informacion_basica_y_formatea_fecha() {
        //Cargamos la vaca sana
        composeTestRule.setContent {
            VacaCard(vaca = vacaSana)
        }

        //Verificamos DIIO
        composeTestRule.onNodeWithText("DIIO: 100").assertIsDisplayed()

        //Verificamos Raza
        composeTestRule.onNodeWithText("Holstein").assertIsDisplayed()

        //Verificamos Ubicación
        composeTestRule.onNodeWithText("Campo Norte").assertIsDisplayed()

        //Verificamos que la funcion formatearFecha funcionó
        //La funcion convierte "2023-05-20..." a "20/05/2023"
        composeTestRule.onNodeWithText("20/05/2023").assertIsDisplayed()

        //Verificamos estado (Badge verde)
        composeTestRule.onNodeWithText("✓ Activo").assertIsDisplayed()
    }

    @Test
    fun muestra_alerta_de_enfermedad_si_vaca_esta_enferma() {
        composeTestRule.setContent {
            VacaCard(vaca = vacaEnferma)
        }

        // Buscamos el texto de la enfermedad
        composeTestRule.onNodeWithText("Mastitis").assertIsDisplayed()

        // Buscamos la etiqueta "Enfermedad:"
        composeTestRule.onNodeWithText("Enfermedad:").assertIsDisplayed()
    }

    @Test
    fun no_muestra_botones_si_mostrarBotones_es_false() {
        composeTestRule.setContent {
            VacaCard(
                vaca = vacaSana,
                mostrarBotones = false // Apagamos los botones
            )
        }

        // Verificamos que los textos de los botones NO existan
        composeTestRule.onNodeWithText("Editar").assertDoesNotExist()
        composeTestRule.onNodeWithText("Eliminar").assertDoesNotExist()
    }

    @Test
    fun click_en_editar_ejecuta_callback() {
        var fueEditada = false

        composeTestRule.setContent {
            VacaCard(
                vaca = vacaSana,
                onEditClick = { fueEditada = true } // Callback de prueba
            )
        }

        // Hacemos click en el botón Editar
        composeTestRule.onNodeWithText("Editar").performClick()

        // Verificamos que la variable cambió
        assert(fueEditada)
    }

    @Test
    fun flujo_completo_de_eliminar_con_dialogo() {
        var fueEliminada = false

        composeTestRule.setContent {
            VacaCard(
                vaca = vacaSana,
                onDeleteClick = { fueEliminada = true }
            )
        }

        //Click en el botón eliminar de la tarjeta
        // Usamos onLast() por seguridad, aunque al principio solo hay uno.
        composeTestRule.onNodeWithText("Eliminar").performClick()

        // Verificamos que APARECIÓ la noti
        composeTestRule.onNodeWithText("¿Eliminar vaca?").assertIsDisplayed()

        // onLast() selecciona el elemento que está más "arriba" en la jerarquía visual (la noti)
        composeTestRule
            .onAllNodesWithText("DIIO: 100")
            .onLast()
            .assertIsDisplayed()

        //Hacemos click en "Eliminar" DENTRO de la noti
        composeTestRule
            .onAllNodesWithText("Eliminar")
            .onLast()
            .performClick()

        //Verificamos el callback
        assert(fueEliminada)
    }
}