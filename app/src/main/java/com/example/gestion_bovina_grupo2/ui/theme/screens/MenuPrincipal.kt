package com.example.gestion_bovina_grupo2.ui.theme.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gestion_bovina_grupo2.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenuPrincipal() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Este sería al menú principal") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 20.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = { /* acción futura */ }) {
                Text(text = "Agregar ramito")
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 300.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
@Preview(name="MenuPrincipal", widthDp = 360, heightDp = 800)
@Composable
fun MenuPrincipal(){
    HomeMenuPrincipal()
}