package com.example.gestion_bovina_grupo2.ui.theme.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
val GreenColor = Color(0xFF237834)
@Composable
fun SplashScreen(navController: NavController) {
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Lógica para navegar al login después de 3 segundos
    LaunchedEffect(Unit) {
        scope.launch {
            loadProgress { progress ->
                currentProgress = progress
            }
            loading = false // Termina la carga cuando el progreso llega a 100%
            delay(1000) // Espera un poco antes de navegar a la pantalla de login
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true } // Elimina la SplashScreen del stack de navegación
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            // Muestra la animación de carga
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(32.dp)
            ) {
                Text(text = "Iniciando la aplicación", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(24.dp))
                LinearProgressIndicator(
                    progress = currentProgress,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = GreenColor
                )
            }
        }
    }
}

/** Itera el valor del progreso */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(25) // Controla la velocidad del progreso
    }
}



@Preview
@Composable
fun PreviewSplashScreen() {
    // Previa de la pantalla de carga, usa un NavController simulado
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}
