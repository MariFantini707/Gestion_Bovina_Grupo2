package com.example.gestion_bovina_grupo2.ui.theme.screens

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.gestion_bovina_grupo2.ui.theme.screens.MenuPrincipal
import com.example.gestion_bovina_grupo2.ui.theme.screens.HomeScreenCompactaa



@Composable
fun HomeScreen2(){
    val windowSizeClass = observeWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompactaa()
    }
}

/*da mucho problema el windowsizeclass*/
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun observeWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as Activity)
}


@Preview(name="Compact", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompact(){
    HomeScreenCompactaa()
}



@Preview(name="MenuPrincipal", widthDp = 360, heightDp = 800)
@Composable
fun HomeMenuCompact(){
    HomeMenuPrincipall()
}

