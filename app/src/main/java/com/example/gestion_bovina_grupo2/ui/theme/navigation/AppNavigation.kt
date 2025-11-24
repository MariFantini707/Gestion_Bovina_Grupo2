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
import com.example.gestion_bovina_grupo2.ViewModel.UsuarioViewModel
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
    val context = LocalContext.current

    // ========== VIEW MODELS ==========

    // UsuarioViewModel (para login y logout)
    val usuarioViewModel = remember {
        UsuarioViewModel(context)
    }

    // Repositorio de DataStore para contadores (tu código existente)
    val contadorRepo = remember { ContadorRepository(context) }

    // VacaViewModel compartido en el grafo (tu código existente)
    val vacaViewModel: VacaViewModel = viewModel(
        factory = VacaViewModel.provideFactory(contadorRepo)
    )

    // ========== FUNCIÓN DE LOGOUT ==========
    val handleLogout: () -> Unit = {
        // Limpiar el token usando el ViewModel
        usuarioViewModel.logout()

        // Navegar al login y limpiar el back stack
        navController.navigate("login") {
            popUpTo(0) { inclusive = true } // Limpia toda la pila de navegación
            launchSingleTop = true
        }
    }

    // ========== NAVEGACIÓN ==========

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(navController = navController)
        }

        // Login Screen
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        // Home Screen (con bottom bar Y logout)
        composable("inicio") {
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        onLogout = handleLogout // ← NUEVO: pasa la función de logout
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeScreen(
                        navController = navController,
                        viewModel = vacaViewModel
                    )
                }
            }
        }

        // Crear Vaca Screen (con bottom bar Y logout)
        composable("crear") {
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        onLogout = handleLogout // ← NUEVO: pasa la función de logout
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CrearVacaScreen(
                        navController = navController
                    )
                }
            }
        }

        // Reportes Screen (con bottom bar Y logout)
        composable("reportes") {
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        onLogout = handleLogout // ← NUEVO: pasa la función de logout
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ReportesScreen(navController = navController)
                }
            }
        }
    }
}