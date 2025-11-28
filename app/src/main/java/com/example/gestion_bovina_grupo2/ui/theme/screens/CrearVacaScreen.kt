package com.example.gestion_bovina_grupo2.ui.theme.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.gestion_bovina_grupo2.data.model.VacaApi
import com.example.gestion_bovina_grupo2.data.model.VacaRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
//pruebas
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import androidx.core.content.FileProvider
import android.net.Uri
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FormularioVacaScreen(
    navController: NavController,
    vacaParaEditar: VacaApi? = null,  // ← null = CREAR, con datos = EDITAR

) {
    val context = LocalContext.current
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(VibratorManager::class.java)
        vibratorManager?.defaultVibrator
    } else {
        context.getSystemService(Vibrator::class.java)
    }


    // vibración
    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrarError() {
        Log.d("VIBRATION", "Vibración ejecutada")
        vibrator?.vibrate(
            VibrationEffect.createOneShot(
                120, // duración ms
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
    //CAMARA

    fun crearArchivoTemporal(): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "vaca_${System.currentTimeMillis()}",
            ".jpg",
            storageDir
        )
    }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var uriTemporal by remember { mutableStateOf<Uri?>(null) }

    val launcherGaleria = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            fotoUri = uri
        }
    }

// CÁMARA REAL


    val launcherCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) fotoUri = uriTemporal
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()



    // ========== ViewModel ==========
    val viewModel = remember { VacaApiViewModel(context) }

    // ========== MODO: CREAR O EDITAR ==========
    val modoEditar = vacaParaEditar != null
    val tituloScreen = if (modoEditar) "Editar vaca 🐄" else "Registrar nueva vaca 🐄"
    val textoBoton = if (modoEditar) "GUARDAR CAMBIOS" else "CREAR VACA"

    // Estados del formulario
    val diio by viewModel.diio.collectAsState()
    val genero by viewModel.genero.collectAsState()
    val raza by viewModel.raza.collectAsState()
    val ubicacion by viewModel.ubicacion.collectAsState()
    val enfermedades by viewModel.enfermedades.collectAsState()

    // Errores
    val diioError by viewModel.diioError.collectAsState()
    val generoError by viewModel.generoError.collectAsState()
    val razaError by viewModel.razaError.collectAsState()
    val ubicacionError by viewModel.ubicacionError.collectAsState()

    // Estados de API
    val isCreating by viewModel.isCreating.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()
    val createError by viewModel.createError.collectAsState()

    val isUpdating by viewModel.isUpdating.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()
    val updateError by viewModel.updateError.collectAsState()

    // Loading combinado
    val isLoading = isCreating || isUpdating

    // ========== DatePicker ==========
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var fechaSeleccionada by remember { mutableStateOf<Long?>(null) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaMostrada = remember(fechaSeleccionada) {
        fechaSeleccionada?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // ========== CARGAR DATOS SI ES EDITAR ==========
    LaunchedEffect(vacaParaEditar) {
        if (vacaParaEditar != null) {
            viewModel.cargarVacaParaEditar(vacaParaEditar)

            // Convertir fecha de String ISO a timestamp
            try {
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                isoFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = isoFormat.parse(vacaParaEditar.dateBirthday)
                fechaSeleccionada = date?.time
            } catch (e: Exception) {
                println("❌ Error al parsear fecha: ${e.message}")
            }
        }
    }

    // ========== Detectar éxito CREAR ==========
    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            snackbarHostState.showSnackbar("✅ Vaca registrada exitosamente!")
            kotlinx.coroutines.delay(500)
            viewModel.resetCreateStates()
            navController.popBackStack()
        }
    }

    // ========== Detectar éxito EDITAR ==========
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            snackbarHostState.showSnackbar("✅ Vaca editada exitosamente!")
            kotlinx.coroutines.delay(500)
            viewModel.resetUpdateStates()
            navController.popBackStack()
        }
    }

    // ========== Detectar errores ==========
    LaunchedEffect(createError) {
        createError?.let { error ->
            snackbarHostState.showSnackbar("❌ $error")
        }
    }

    LaunchedEffect(updateError) {
        updateError?.let { error ->
            snackbarHostState.showSnackbar("❌ $error")
        }
    }

    // Dropdown género
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
                title = { Text(tituloScreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // ← FLECHA ATRÁS
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
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
                enabled = !isLoading && !modoEditar
            )
            if (diioError != null) {
                Text(diioError!!, color = Color.Red, fontSize = 12.sp)
            }
            if (modoEditar) {
                Text(
                    "El DIIO no se puede modificar",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // FECHA
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
                enabled = !isLoading
            )

            // GÉNERO
            ExposedDropdownMenuBox(
                expanded = generoExpanded,
                onExpandedChange = { generoExpanded = !generoExpanded && !isLoading }
            ) {
                TextField(
                    value = generoDisplay(genero),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    isError = generoError != null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    enabled = !isLoading
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

            // RAZA
            OutlinedTextField(
                value = raza,
                onValueChange = { viewModel.onRazaChange(it) },
                label = { Text("Raza") },
                isError = razaError != null,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            if (razaError != null) {
                Text(razaError!!, color = Color.Red, fontSize = 12.sp)
            }

            // UBICACIÓN
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { viewModel.onUbicacionChange(it) },
                label = { Text("Ubicación") },
                isError = ubicacionError != null,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            if (ubicacionError != null) {
                Text(ubicacionError!!, color = Color.Red, fontSize = 12.sp)
            }

            // ENFERMEDADES
            OutlinedTextField(
                value = enfermedades,
                onValueChange = { viewModel.onEnfermedadesChange(it) },
                label = { Text("Enfermedades (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            //COMIENZO DE LA CAMARA

            val permisoCamara = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    val archivo = crearArchivoTemporal()
                    uriTemporal = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        archivo
                    )
                    launcherCamara.launch(uriTemporal!!)
                }
            }

            Text(
                "Ingrese fotografía de la vaca (opcional)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        launcherGaleria.launch("image/*")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Galería")
                }

                Button(
                    onClick = {
                        permisoCamara.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cámara")
                }
            }

            Spacer(Modifier.height(12.dp))

            // Vista previa de la imagen seleccionada
            fotoUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Foto vaca",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Spacer(Modifier.height(12.dp))
            //FIN DE LA CAMARA

            // ========== BOTÓN CREAR/EDITAR ==========
            Button(
                onClick = {
                    // Validar fecha
                    if (fechaSeleccionada == null) {
                        vibrarError()
                        scope.launch {
                            snackbarHostState.showSnackbar("❌ Por favor selecciona una fecha")
                        }
                        return@Button
                    }

                    // Validar formulario
                    if (!viewModel.validarFormulario()) {
                        vibrarError()
                        scope.launch {
                            snackbarHostState.showSnackbar("❌ Por favor completa todos los campos")
                        }
                        return@Button
                    }

                    // Enviar
                    scope.launch {
                        try {
                            // Convertir fecha
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

                            // CREAR o EDITAR
                            if (modoEditar) {
                                println("🚀 Editando vaca ID: ${vacaParaEditar!!.id}")
                                viewModel.editarVaca(vacaParaEditar.id, vacaRequest)
                            } else {
                                println("🚀 Creando nueva vaca")
                                viewModel.crearVaca(vacaRequest)
                            }

                        } catch (e: Exception) {
                            println("❌ Error: ${e.message}")
                            e.printStackTrace()
                            snackbarHostState.showSnackbar("❌ Error: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (modoEditar) Color(0xFFFFA500) else Color(0xFF1DB954),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (modoEditar) "GUARDANDO..." else "CREANDO...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(textoBoton, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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