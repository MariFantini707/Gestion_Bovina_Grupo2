package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestion_bovina_grupo2.ViewModel.VacaApiViewModel
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearVacaScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ========== SOLO VacaApiViewModel ==========
    val viewModel = remember { VacaApiViewModel(context) }

    // Estados del formulario
    val diio by viewModel.diio.collectAsState()
    val genero by viewModel.genero.collectAsState()
    val raza by viewModel.raza.collectAsState()
    val ubicacion by viewModel.ubicacion.collectAsState()
    val enfermedades by viewModel.enfermedades.collectAsState()

    // Errores de validación
    val diioError by viewModel.diioError.collectAsState()
    val generoError by viewModel.generoError.collectAsState()
    val razaError by viewModel.razaError.collectAsState()
    val ubicacionError by viewModel.ubicacionError.collectAsState()

    // Estados de API
    val isCreating by viewModel.isCreating.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()
    val createError by viewModel.createError.collectAsState()

    // ========== DatePicker State ==========
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaMostrada = remember(fechaSeleccionada) {
        fechaSeleccionada?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // ========== Detectar éxito y navegar ==========
    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            snackbarHostState.showSnackbar("✅ Vaca registrada exitosamente!")
            kotlinx.coroutines.delay(1500)
            viewModel.resetCreateStates()
            navController.popBackStack()
        }
    }

    // ========== Detectar error ==========
    LaunchedEffect(createError) {
        createError?.let { error ->
            snackbarHostState.showSnackbar("❌ $error")
        }
    }

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
                value = diio,
                onValueChange = { viewModel.onDiioChange(it) },
                label = { Text("DIIO") },
                isError = diioError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            )
            if (diioError != null) {
                Text(diioError!!, color = Color.Red, fontSize = 12.sp)
            }

            // ========== FECHA CON CALENDARIO ==========
            OutlinedTextField(
                value = fechaMostrada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de Nacimiento") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, "Seleccionar fecha")
                    }
                },
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            )

            // Género (select / dropdown)
            ExposedDropdownMenuBox(
                expanded = generoExpanded,
                onExpandedChange = { generoExpanded = !generoExpanded && !isCreating }
            ) {
                TextField(
                    value = generoDisplay(genero),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    isError = generoError != null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = !isCreating
                )
                ExposedDropdownMenu(
                    expanded = generoExpanded,
                    onDismissRequest = { generoExpanded = false }
                ) {
                    generoOptions.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                viewModel.onGeneroChange(code)
                                generoExpanded = false
                            }
                        )
                    }
                }
            }
            if (generoError != null) {
                Text(generoError!!, color = Color.Red, fontSize = 12.sp)
            }

            // Raza
            OutlinedTextField(
                value = raza,
                onValueChange = { viewModel.onRazaChange(it) },
                label = { Text("Raza") },
                isError = razaError != null,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            )
            if (razaError != null) {
                Text(razaError!!, color = Color.Red, fontSize = 12.sp)
            }

            // Ubicación
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { viewModel.onUbicacionChange(it) },
                label = { Text("Ubicación") },
                isError = ubicacionError != null,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            )
            if (ubicacionError != null) {
                Text(ubicacionError!!, color = Color.Red, fontSize = 12.sp)
            }

            // Enfermedades
            OutlinedTextField(
                value = enfermedades,
                onValueChange = { viewModel.onEnfermedadesChange(it) },
                label = { Text("Enfermedades (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ========== BOTÓN CREAR ==========
            Button(
                onClick = {
                    // 1. Validar fecha
                    if (fechaSeleccionada == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("❌ Por favor selecciona una fecha")
                        }
                        return@Button
                    }

                    // 2. Validar formulario
                    if (!viewModel.validarFormulario()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("❌ Por favor completa todos los campos")
                        }
                        return@Button
                    }

                    // 3. Enviar al API
                    scope.launch {
                        try {
                            // Convertir fecha a ISO 8601
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = fechaSeleccionada!!
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            val fechaFormateada = isoFormat.format(calendar.time)

                            // Convertir género
                            val generoAPI = when(genero) {
                                "m" -> "M"
                                "h" -> "F"
                                else -> "M"
                            }

                            // Crear request
                            val vacaRequest = VacaRequest(
                                diio = diio.toInt(),
                                dateBirthday = fechaFormateada,
                                genre = generoAPI,
                                race = raza,
                                location = ubicacion,
                                sick = if (enfermedades.isEmpty()) null else enfermedades
                            )

                            println("🚀 Enviando vaca al API...")
                            println("   DIIO: ${vacaRequest.diio}")
                            println("   Fecha: ${vacaRequest.dateBirthday}")
                            println("   Género: ${vacaRequest.genre}")
                            println("   Raza: ${vacaRequest.race}")
                            println("   Ubicación: ${vacaRequest.location}")
                            println("   Enfermedad: ${vacaRequest.sick ?: "Ninguna"}")

                            viewModel.crearVaca(vacaRequest)

                        } catch (e: Exception) {
                            println("❌ Error: ${e.message}")
                            e.printStackTrace()
                            snackbarHostState.showSnackbar("❌ Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isCreating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CREANDO...", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("CREAR VACA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // ========== DIÁLOGO DATE PICKER ==========
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        fechaSeleccionada = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}