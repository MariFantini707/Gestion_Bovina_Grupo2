package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gestion_bovina_grupo2.R
import com.example.gestion_bovina_grupo2.ViewModel.UsuarioViewModel
import com.example.gestion_bovina_grupo2.ui.theme.GreenColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    // Observamos el flujo de datos del ViewModel
    val estado by viewModel.estado.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        modifier = Modifier.background(color = GreenColor)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Fit
                )

                // Título
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                // Campo Email
                OutlinedTextField(
                    value = estado.email,
                    onValueChange = { nuevoValor ->
                        viewModel.onEmailChange(nuevoValor.trim())
                    },
                    label = { Text("Correo electrónico") },
                    isError = estado.errores.email != null,
                    supportingText = {
                        estado.errores.email?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Password
                OutlinedTextField(
                    value = estado.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.password != null,
                    supportingText = {
                        estado.errores.password?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Login
                Button(
                    onClick = {
                        viewModel.validarYLogin {
                            // Navegar al inicio cuando el login sea exitoso
                            navController.navigate("inicio") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1DB954),  // Verde Spotify
                        contentColor = Color.White            // Texto blanco
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar sesión")
                    }
                }

                // Mensaje de error general
                estado.errores.loginGeneral?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {}