package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestion_bovina_grupo2.ViewModel.VacaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearVacaScreen(
    navController: NavController,
    viewModel: VacaViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Dropdown de género
    var generoExpanded by remember { mutableStateOf(false) }
    val generoOptions = listOf(
        "m" to "Macho (m)",
        "h" to "Hembra (h)"
    )
    fun generoDisplay(code: String): String =
        generoOptions.firstOrNull { it.first == code }?.second ?: ""

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registrar nueva vaca 🐄", fontWeight = FontWeight.Bold) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DIIO
            OutlinedTextField(
                value = estado.diio,
                onValueChange = { viewModel.onDiioChange(it) },
                label = { Text("DIIO") },
                isError = estado.erroresVaca.diio != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (estado.erroresVaca.diio != null) {
                Text(estado.erroresVaca.diio!!, color = Color.Red, fontSize = 12.sp)
            }

            // Fecha
            OutlinedTextField(
                value = estado.fecha,
                onValueChange = { viewModel.onFechaChange(it) },
                label = { Text("Fecha dd/mm/yyyy") },
                isError = estado.erroresVaca.fecha != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (estado.erroresVaca.fecha != null) {
                Text(estado.erroresVaca.fecha!!, color = Color.Red, fontSize = 12.sp)
            }

            // Género (select / dropdown)
            ExposedDropdownMenuBox(
                expanded = generoExpanded,
                onExpandedChange = { generoExpanded = !generoExpanded }
            ) {
                TextField(
                    value = generoDisplay(estado.genero),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    isError = estado.erroresVaca.genero != null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = generoExpanded,
                    onDismissRequest = { generoExpanded = false }
                ) {
                    generoOptions.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                viewModel.onGeneroChange(code) // guarda "m" o "h"
                                generoExpanded = false
                            }
                        )
                    }
                }
            }
            if (estado.erroresVaca.genero != null) {
                Text(estado.erroresVaca.genero!!, color = Color.Red, fontSize = 12.sp)
            }

            // Raza
            OutlinedTextField(
                value = estado.raza,
                onValueChange = { viewModel.onRazaChange(it) },
                label = { Text("Raza") },
                isError = estado.erroresVaca.raza != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (estado.erroresVaca.raza != null) {
                Text(estado.erroresVaca.raza!!, color = Color.Red, fontSize = 12.sp)
            }

            // Ubicación
            OutlinedTextField(
                value = estado.ubicacion,
                onValueChange = { viewModel.onUbicacionChange(it) },
                label = { Text("Ubicación") },
                isError = estado.erroresVaca.ubicacion != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (estado.erroresVaca.ubicacion != null) {
                Text(estado.erroresVaca.ubicacion!!, color = Color.Red, fontSize = 12.sp)
            }

            // Enfermedades
            OutlinedTextField(
                value = estado.enfermedades,
                onValueChange = { viewModel.onEnfermedadesChange(it) },
                label = { Text("Enfermedades (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.crearVaca()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("✓ Vaca registrada con éxito!")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("❌ Corrige los errores del formulario")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
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
