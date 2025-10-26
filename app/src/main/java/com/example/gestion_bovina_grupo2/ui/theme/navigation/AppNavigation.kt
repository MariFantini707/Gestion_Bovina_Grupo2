// ui/theme/navigation/AppNavigation.kt
package com.example.gestion_bovina_grupo2.ui.theme.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestion_bovina_grupo2.ui.theme.screens.CrearVacaScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.HomeScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.LoginScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.ReportesScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login (SIN menú)
        composable("login") {
            LoginScreen(navController = navController)
        }

        // Home (CON menú)
        composable("inicio") {
            Scaffold(
                bottomBar = { BottomNavBar(navController = navController) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeScreen(navController = navController)
                }
            }
        }

        // Alias para home
        composable("home") {
            Scaffold(
                bottomBar = { BottomNavBar(navController = navController) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeScreen(navController = navController)
                }
            }
        }

        // Crear Vaca (SIN menú, tiene botón atrás)
        composable("crear") {
            CrearVacaScreen(navController = navController)
        }

        // Reportes (CON menú)
        composable("reportes") {
            Scaffold(
                bottomBar = { BottomNavBar(navController = navController) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ReportesScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun ReportesScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun CrearVacaScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun HomeScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    TODO("Not yet implemented")
}

@Composable
fun LoginScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}