package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestion_bovina_grupo2.ViewModel.VacaViewModel
import com.example.gestion_bovina_grupo2.ui.theme.composables.VacaCard
import com.example.gestion_bovina_grupo2.ViewModel.VacaApiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: VacaViewModel
) {
    val context = LocalContext.current

    val vacaApiViewModel = remember {
        VacaApiViewModel(context)
    }

    // Observar estados del API
    val vacas by vacaApiViewModel.vacas.collectAsState()
    val isLoading by vacaApiViewModel.isLoading.collectAsState()
    val error by vacaApiViewModel.error.collectAsState()

    // ========== ESTADO DEL FILTRO ==========
    var filtroActivo by remember { mutableStateOf(true) }

    // ========== OBTENER VACAS AL INICIAR (activas por defecto) ==========
    LaunchedEffect(Unit) {
        println("🚀 HomeScreen iniciado - Obteniendo vacas activas...")
        vacaApiViewModel.obtenerVacasActivas()
    }

    // ========== CAMBIAR ENDPOINT CUANDO CAMBIA EL FILTRO ==========
    LaunchedEffect(filtroActivo) {
        if (filtroActivo) {
            println("🔍 Filtro cambiado a: Activas - Llamando GET /vacas")
            vacaApiViewModel.obtenerVacasActivas()
        } else {
            println("🔍 Filtro cambiado a: Desactivadas - Llamando GET /vacas/desactivadas")
            vacaApiViewModel.obtenerVacasDesactivadas()
        }
    }

    val hoy by viewModel.registradasHoy.collectAsState()

    // Fecha de hoy formateada
    val fechaHoy = remember {
        val sdf = SimpleDateFormat("EEEE d 'de' MMMM yyyy", Locale.getDefault())
        sdf.format(Date()).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 0.dp)
    ) {
        // ========== ENCABEZADO ==========
        Text(
            text = "Hola, Administrador 👋",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = fechaHoy,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ========== KPIs ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard(
                title = "Total de vacas",
                value = vacas.size.toString(),
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Registradas hoy",
                value = hoy.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ========== FILTRO DE VACAS ==========
        FiltroVacas(
            filtroActivo = filtroActivo,
            onFiltroChange = { nuevoFiltro ->
                filtroActivo = nuevoFiltro
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ========== TÍTULO DE LISTA ==========
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (filtroActivo) "Vacas activas" else "Vacas desactivadas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "(${vacas.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ========== LISTA DE VACAS ==========
        when {
            // Cargando
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (filtroActivo)
                                "Cargando vacas activas..."
                            else
                                "Cargando vacas desactivadas...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Error
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "❌",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Error al cargar vacas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = {
                                if (filtroActivo) {
                                    vacaApiViewModel.obtenerVacasActivas()
                                } else {
                                    vacaApiViewModel.obtenerVacasDesactivadas()
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            // Lista vacía
            vacas.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = if (filtroActivo) "🐄" else "😴",
                            fontSize = 64.sp
                        )
                        Text(
                            text = if (filtroActivo)
                                "No hay vacas activas"
                            else
                                "No hay vacas desactivadas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (filtroActivo)
                                "Todas tus vacas están desactivadas o aún no has registrado ninguna"
                            else
                                "Todas tus vacas están activas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Lista con vacas
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(vacas) { vaca ->
                        VacaCard(
                            vaca = vaca,
                            mostrarBotones = filtroActivo,
                            onEditClick = { vacaSeleccionada ->
                                println("✏️ Editar vaca: ${vacaSeleccionada.diio}")
                                // TODO: Navegar a pantalla de edición
                            },
                            onDeleteClick = { vacaSeleccionada ->
                                println("🗑️ Eliminar vaca: ${vacaSeleccionada.diio}")
                                // TODO: Implementar eliminación
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente de filtro con dos botones (Activas / Desactivadas)
 */
@Composable
private fun FiltroVacas(
    filtroActivo: Boolean,
    onFiltroChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón Activas
        FilledTonalButton(
            onClick = { onFiltroChange(true) },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (filtroActivo) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                contentColor = if (filtroActivo) Color.White else Color(0xFF757575)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Activas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Botón Desactivadas
        FilledTonalButton(
            onClick = { onFiltroChange(false) },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (!filtroActivo) Color(0xFFFF5252) else Color(0xFFE0E0E0),
                contentColor = if (!filtroActivo) Color.White else Color(0xFF757575)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Desactivadas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}