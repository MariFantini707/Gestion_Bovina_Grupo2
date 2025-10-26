package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearVacaScreen(navController: NavController) {
    var codigo by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    // Estados de error
    var nombreError by remember { mutableStateOf(false) }
    var codigoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registrar Nueva Vaca",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1DB954),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Complete los datos de la vaca",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Campo Código
            OutlinedTextField(
                value = codigo,
                onValueChange = {
                    codigo = it.uppercase()
                    codigoError = false
                },
                label = { Text("Código de identificación *") },
                placeholder = { Text("Ej: COD-001") },
                isError = codigoError,
                supportingText = {
                    if (codigoError) {
                        Text(
                            "El código es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Raza
            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                label = { Text("Raza") },
                placeholder = { Text("Ej: Holstein, Jersey, Angus") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campos en fila (Edad y Peso)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Edad
                OutlinedTextField(
                    value = edad,
                    onValueChange = {
                        // Solo permite números
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            edad = it
                        }
                    },
                    label = { Text("Edad (años)") },
                    placeholder = { Text("Ej: 3") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                // Peso
                OutlinedTextField(
                    value = peso,
                    onValueChange = {
                        // Solo permite números y un punto decimal
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            peso = it
                        }
                    },
                    label = { Text("Peso (kg)") },
                    placeholder = { Text("Ej: 450") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            // Campo Observaciones
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones") },
                placeholder = { Text("Notas adicionales sobre la vaca...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Card informativa
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "* Campos obligatorios",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = {
                    // Validar campos obligatorios
                    codigoError = codigo.isBlank()

                    if (!codigoError) {
                        // Aquí guardarías los datos (ViewModelo, BD, etc.)
                        // Por ahora solo vuelve atrás
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954),  // Verde Spotify
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "GUARDAR VACA",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Botón Cancelar
            OutlinedButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CANCELAR")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}