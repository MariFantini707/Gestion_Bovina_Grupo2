package com.example.gestion_bovina_grupo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import com.example.gestion_bovina_grupo2.ui.theme.navigation.AppNavigation

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

@Composable
fun Gestion_Bovina_Grupo2Theme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

