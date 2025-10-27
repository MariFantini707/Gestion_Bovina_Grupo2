package com.example.gestion_bovina_grupo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

import com.example.gestion_bovina_grupo2.ui.theme.Gestion_Bovina_Grupo2Theme
import com.example.gestion_bovina_grupo2.ui.theme.navigation.AppNavigation
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Gestion_Bovina_Grupo2Theme {
                AppNavigation()
            }
        }
    }
}

// Preview de la app completa
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "App Completa"
)
@Composable
fun AppPreview() {
    Gestion_Bovina_Grupo2Theme {
        AppNavigation()
    }
}
