package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.gestion_bovina_grupo2.R

@Composable
fun HomeScreen() {
    Column(
        Modifier.fillMaxSize()
    ) {
        // Cabecera con logo
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Asegúrate de tener estos drawables en res/drawable
            Image(
                painter = painterResource(id = R.drawable.colun_logo), // <- pon tu recurso
                contentDescription = "Logo Colun",
                modifier = Modifier.height(42.dp)
            )
            Spacer(Modifier.weight(1f))
            // (No hay menú hamburguesa)
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = buildAnnotatedString {
                append("Bienvenido ")
                withStyle(MaterialTheme.typography.headlineMedium.toSpanStyle().copy(fontWeight = FontWeight.Bold)) {
                    append("administrador")
                }
                append("!")
            },
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // El resto queda vacío (como tu mock). Puedes agregar tarjetas/resumen luego.
    }
}
6) Form (placeholder para probar navegación)
kotlin
Copiar código
package com.example.gestion_bovina_grupo2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Formulario", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Aquí irá el alta de vacas y validaciones.")
    }
}