package com.example.gestion_bovina_grupo2.ui.theme.navigation.screen

sealed class NavigationEvent {
    data class NavigateTo(
        val route: Screens,
        val popUpToRoute: Screens? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) :NavigationEvent()

    object PopBackStack : NavigationEvent()

    object NavigateUp : NavigationEvent()
}