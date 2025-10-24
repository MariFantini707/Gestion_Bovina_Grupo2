package com.example.gestion_bovina_grupo2.ui.theme.screens

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.gestion_bovina_grupo2.ui.theme.screens.MenuPrincipal
import com.example.gestion_bovina_grupo2.ui.theme.screens.HomeScreenCompacta
import observeWindowSizeClass


@Composable
fun HomeScreen2(){
    val windowSizeClass = observeWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
    }
}


@Preview(name="Compact", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompact(){
    HomeScreenCompacta()
}

@Composable
fun HomeScreenCompacta() {
    TODO("Not yet implemented")
}

@Preview(name="MenuPrincipal", widthDp = 360, heightDp = 800)
@Composable
fun HomeMenuCompact(){
    HomeMenuPrincipal()
}
@Preview(name="MenuPrincipal", widthDp = 360, heightDp = 800)
@Composable
fun HomeMenuPrincipal() {
    HomeMenuPrincipal()
}