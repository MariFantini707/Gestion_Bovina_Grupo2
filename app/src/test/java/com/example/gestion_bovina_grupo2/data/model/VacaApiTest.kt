package com.example.gestion_bovina_grupo2.data.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VacaApiTest {

    @Test
    fun `getGeneroLegible retorna Hembra cuando el genero es F mayuscula`() {
        // Crear vaca con género F
        val vaca = crearVacaDummy(genero = "F")

        // Verificar
        assertEquals("Hembra", vaca.getGeneroLegible())
    }

    @Test
    fun `getGeneroLegible retorna Macho cuando el genero es M mayuscula`() {
        val vaca = crearVacaDummy(genero = "M")
        assertEquals("Macho", vaca.getGeneroLegible())
    }

    @Test
    fun `getGeneroLegible retorna Desconocido cuando el genero no es F ni M`() {
        // Casos raros: minúsculas, vacíos o letras extrañas
        assertEquals("Desconocido", crearVacaDummy("f").getGeneroLegible()) // Minúscula
        assertEquals("Desconocido", crearVacaDummy("X").getGeneroLegible()) // Letra rara
        assertEquals("Desconocido", crearVacaDummy("").getGeneroLegible())  // Vacío
    }

    //Escribimos una función de ayuda porque ahorra escribir todos los campos obligatorios en cada test
    private fun crearVacaDummy(genero: String): VacaApi {
        return VacaApi(
            id = "1",
            diio = 123,
            dateBirthday = "2024-01-01",
            genre = genero,
            race = "Test",
            location = "Test",
            sick = null,
            cowState = true
        )
    }
}