package com.example.gestion_bovina_grupo2.ui.theme.navigation.screen

sealed class Screens(val route: String) {
    data object Home : Screens("home_page")

    data object Profile : Screens("profile_page")

    data object Settings : Screens("settings_page")

    data class Detail(val itemId: String) : Screens("detail_page/{itemId}"){
        fun buildRoute(): String{
            return route.replace("{itemId}",itemId)
        }
    }
}