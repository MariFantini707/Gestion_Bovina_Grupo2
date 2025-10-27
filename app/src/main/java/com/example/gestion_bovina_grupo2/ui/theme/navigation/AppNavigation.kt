package com.example.gestion_bovina_grupo2.ui.theme.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestion_bovina_grupo2.ViewModel.VacaViewModel
import com.example.gestion_bovina_grupo2.repository.ContadorRepository
import com.example.gestion_bovina_grupo2.ui.theme.composables.SplashScreen

import com.example.gestion_bovina_grupo2.ui.theme.screens.CrearVacaScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.HomeScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.LoginScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.ReportesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 1) Repositorio de DataStore para contadores
    val context = LocalContext.current
    val contadorRepo = remember(context) { ContadorRepository(context) }

    // 2) VacaViewModel compartido en el grafo usando la factory que recibe el repo
    val vacaViewModel: VacaViewModel = viewModel(
        factory = VacaViewModel.provideFactory(contadorRepo)
    )

    // 3) Grafo de navegación
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }


        // Puedes mantener ambas rutas a Home si ya las usabas
        composable("inicio") {
            Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeScreen(
                        navController = navController,
                        viewModel = vacaViewModel
                    )
                }
            }
        }


        composable("home") {
            Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeScreen(
                        navController = navController,
                        viewModel = vacaViewModel
                    )
                }
            }
        }


        composable("crear") {
            Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CrearVacaScreen(
                        navController = navController,
                        viewModel = vacaViewModel
                    )
                }
            }
        }

        composable("reportes") {
            Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ReportesScreen(navController = navController)
                }
            }
        }
    }
}
