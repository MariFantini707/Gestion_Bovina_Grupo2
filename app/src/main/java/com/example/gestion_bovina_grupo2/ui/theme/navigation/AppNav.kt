package com.example.gestion_bovina_grupo2.ui.theme.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gestion_bovina_grupo2.ui.screens.FormScreen
import com.example.gestion_bovina_grupo2.ui.screens.HomeScreen
import com.example.gestion_bovina_grupo2.ui.screens.LoginApp
import com.example.gestion_bovina_grupo2.ui.screens.MenuPrincipal

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route
    val showBottomBar = current in setOf(Route.Home.route, Route.Form.route)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MenuPrincipal(
                    currentRoute = current,
                    onNavigate = { route ->
                        if (route != current) {
                            nav.navigate(route) {
                                launchSingleTop = true
                                popUpTo(Route.Home.route)
                            }
                        }
                    }
                )
            }
        }
    ) { inner ->
        NavHost(
            navController = nav,
            startDestination = Route.Login.route,
            modifier = Modifier.padding(inner)
        ) {
            composable(Route.Login.route) {
                LoginApp(
                    onLoginSuccess = {
                        nav.navigate(Route.Home.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Route.Home.route) { HomeScreen() }
            composable(Route.Form.route) { FormScreen() }
        }
    }
}
