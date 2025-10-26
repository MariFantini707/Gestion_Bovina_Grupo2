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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gestion_bovina_grupo2.ViewModel.VacaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearVacaScreen(
    navController: NavController,
    viewModel: VacaViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Registrar Nueva Vaca", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
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

            Text(
                text = "El * significa obligatorio",
                fontSize = 14.sp,
                color = Color(0xFFB2ACAC),
                fontWeight = FontWeight.Bold
            )

            // DIIO
            OutlinedTextField(
                value = estado.diio,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        viewModel.onDiioChange(it)
                    }
                },
                label = { Text("* DIIO") },
                placeholder = { Text("Solo números") },
                isError = estado.erroresVaca.diio != null,
                supportingText = {
                    estado.erroresVaca.diio?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Fecha
            OutlinedTextField(
                value = estado.fecha,
                onValueChange = viewModel::onFechaChange,
                label = { Text("* Fecha de Nacimiento") },
                placeholder = { Text("DD/MM/AAAA") },
                isError = estado.erroresVaca.fecha != null,
                supportingText = {
                    estado.erroresVaca.fecha?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = { Text("📅") }
            )

            // Género
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = when(estado.genero) {
                        "m" -> "Macho"
                        "h" -> "Hembra"
                        else -> "Seleccione género"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("* Género") },
                    isError = estado.erroresVaca.genero != null,
                    supportingText = {
                        estado.erroresVaca.genero?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("Macho" to "m", "Hembra" to "h").forEach { (label, value) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                viewModel.onGeneroChange(value)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Raza
            OutlinedTextField(
                value = estado.raza,
                onValueChange = viewModel::onRazaChange,
                label = { Text("* Raza") },
                placeholder = { Text("Ingrese la raza de la vaca") },
                isError = estado.erroresVaca.raza != null,
                supportingText = {
                    estado.erroresVaca.raza?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Ubicación
            OutlinedTextField(
                value = estado.ubicacion,
                onValueChange = viewModel::onUbicacionChange,
                label = { Text("* Ubicación") },
                placeholder = { Text("Ingrese ubicación de la vaca") },
                isError = estado.erroresVaca.ubicacion != null,
                supportingText = {
                    estado.erroresVaca.ubicacion?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Enfermedades
            Column {
                Text("Enfermedades", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Máx. 150 caracteres", fontSize = 12.sp, color = Color(0xFFB2ACAC))
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = estado.enfermedades,
                    onValueChange = viewModel::onEnfermedadesChange,
                    placeholder = { Text("Escriba aquí...") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5,
                    supportingText = {
                        Text(
                            "${estado.enfermedades.length}/150",
                            fontSize = 12.sp,
                            color = if (estado.enfermedades.length >= 150)
                                MaterialTheme.colorScheme.error else Color.Gray
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Crear
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        viewModel.limpiarFormulario()
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CREAR VACA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}