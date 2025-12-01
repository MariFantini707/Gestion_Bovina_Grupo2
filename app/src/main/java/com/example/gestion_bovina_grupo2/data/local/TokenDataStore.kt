package com.example.gestion_bovina_grupo2.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenDataStore(private val context: Context) {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    // Guardar token
    suspend fun guardarToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    // Obtener token
    suspend fun obtenerToken(): String? {
        return context.dataStore.data.map { it[TOKEN_KEY] }.first()
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}