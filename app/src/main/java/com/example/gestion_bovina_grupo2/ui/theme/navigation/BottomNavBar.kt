package com.example.gestion_bovina_grupo2.ui.theme.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.savedstate.savedState

// Como una interfaz en TypeScript
data class BottomNavItem(
    val route: String,      // ej: "inicio"
    val label: String,      // ej: "Inicio"
    val icon: ImageVector   // ej: Icons.Filled.Home
)

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Obtener la ruta actual (qué pantalla estás viendo)
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    // Definir los items del menú (tus 3 botones)
    val items = listOf(
        BottomNavItem(
            route = "inicio",
            label = "Inicio",
            icon = Icons.Filled.Home
        ),
        BottomNavItem(
            route = "crear",
            label = "Crear",
            icon = Icons.Filled.Add
        ),
        BottomNavItem(
            route = "reportes",
            label = "Reportes",
            icon = Icons.Filled.Assessment
        )
    )

    // Crear el contenedor del menú
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF1DB954),
        contentColor = Color.White
    ) {
        // Por cada item, crear un botón
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,           // Icono seleccionado: blanco
                    selectedTextColor = Color.White,           // Texto seleccionado: blanco
                    unselectedIconColor = Color.White,   // Icono no seleccionado: gris claro
                    unselectedTextColor = Color.White,   // Texto no seleccionado: gris claro
                    indicatorColor = Color(0xFF1ED760)         // Fondo del seleccionado: verde más claro
                )
            )
        }
    }
}

