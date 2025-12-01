package com.example.gestion_bovina_grupo2.ui.theme.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestion_bovina_grupo2.ViewModel.UsuarioViewModel
import com.example.gestion_bovina_grupo2.ViewModel.VacaViewModel
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.repository.ContadorRepository
import com.example.gestion_bovina_grupo2.ui.theme.composables.SplashScreen
import com.example.gestion_bovina_grupo2.ui.theme.screens.FormularioVacaScreen
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

    // ✅ NUEVO - Observar estado de autenticación
    val isAuthenticated by usuarioViewModel.isAuthenticated.collectAsState()

    // Repositorio de DataStore para contadores
    val contadorRepo = remember { ContadorRepository(context) }

    // VacaViewModel compartido en el grafo
    val vacaViewModel: VacaViewModel = viewModel(
        factory = VacaViewModel.provideFactory(contadorRepo)
    )

    // ========== FUNCIÓN DE LOGOUT ==========
    val handleLogout: () -> Unit = {
        usuarioViewModel.logout()
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    // ========== NAVEGACIÓN ==========

    NavHost(
        navController = navController,
        // ✅ CAMBIO IMPORTANTE - Inicia según estado de autenticación
        startDestination = if (isAuthenticated) "inicio" else "splash"
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
                        onLogout = handleLogout
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
            val vaca = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<VacaApi>("vaca")

            FormularioVacaScreen(
                navController = navController,
                vacaParaEditar = vaca
            )
        }

        // Reportes Screen (con bottom bar Y logout)
        composable("reportes") {
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        navController = navController,
                        onLogout = handleLogout
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