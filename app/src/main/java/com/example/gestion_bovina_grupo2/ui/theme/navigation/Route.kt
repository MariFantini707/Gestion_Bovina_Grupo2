package com.example.gestion_bovina_grupo2.ui.theme.navigation

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Home : Route("home")
    data object Form : Route("form")
}