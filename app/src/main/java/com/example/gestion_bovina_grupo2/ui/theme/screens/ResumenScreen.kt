package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.gestion_bovina_grupo2.ViewModel.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel){
    val estado by viewModel.estado.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Resumen del registro", style = MaterialTheme.typography.headlineMedium)
        Text("Correo:${estado.email}")
        Text("Contraseña: ${"*".repeat(estado.password.length)}")
    }
}
