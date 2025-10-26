package com.example.gestion_bovina_grupo2.ui.theme.navigation.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.gestion_bovina_grupo2.ViewModel.UsuarioViewModel
import com.example.gestion_bovina_grupo2.ui.theme.screens.LoginScreen


@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val usuarioViewModel : UsuarioViewModel = viewModel ()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ){
        composable("registro"){
            LoginScreen(navController, usuarioViewModel)
        }

    }
}




