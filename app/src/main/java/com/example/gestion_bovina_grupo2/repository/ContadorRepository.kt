package com.example.gestion_bovina_grupo2.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DS_NAME = "vacas_counters"
private val Context.dataStore by preferencesDataStore(name = DS_NAME)

private object Keys {
    val TOTAL = intPreferencesKey("total_vacas")
    val HOY   = intPreferencesKey("registradas_hoy")
}

data class Contadores(val total: Int, val hoy: Int)

class ContadorRepository(private val context: Context) {

    private val DEFAULT_TOTAL = 23
    private val DEFAULT_HOY = 0

    /** Lee una vez (con valores por defecto si aún no existen). */
    suspend fun getCountersOnce(): Contadores {
        val prefs = context.dataStore.data.first()
        return Contadores(
            total = prefs[Keys.TOTAL] ?: DEFAULT_TOTAL,
            hoy   = prefs[Keys.HOY]   ?: DEFAULT_HOY
        )
    }

    /** Observa cambios (opcional por si quieres trabajar reactivo). */
    fun observeCounters() = context.dataStore.data.map { prefs ->
        Contadores(
            total = prefs[Keys.TOTAL] ?: DEFAULT_TOTAL,
            hoy   = prefs[Keys.HOY]   ?: DEFAULT_HOY
        )
    }

    /** Suma +1 a ambos contadores de forma atómica y devuelve los nuevos valores. */
    suspend fun incrementOnVacaGuardada(): Contadores {
        var result: Contadores? = null
        context.dataStore.edit { mut ->
            val newTotal = (mut[Keys.TOTAL] ?: DEFAULT_TOTAL) + 1
            val newHoy   = (mut[Keys.HOY]   ?: DEFAULT_HOY) + 1
            mut[Keys.TOTAL] = newTotal
            mut[Keys.HOY]   = newHoy
            result = Contadores(newTotal, newHoy)
        }
        return result!!
    }

    /** Set explícito (por si un día quieres resetear “hoy”). */
    suspend fun setCounters(total: Int, hoy: Int) {
        context.dataStore.edit {
            it[Keys.TOTAL] = total
            it[Keys.HOY]   = hoy
        }
    }
}


