package com.example.gestion_bovina_grupo2.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que contiene la configuración de Retrofit
 * Se instancia el servicio de la API una sola vez
 */
object RetrofitInstance {

    // IMPORTANTE: Cambiar URL segun maquina de despliegue
    // Para EMULADOR de Android Studio: usa "10.0.2.2"
    // Para DISPOSITIVO FÍSICO: usa tu IP local (ej: "192.168.1.110")
    private const val BASE_URL = "https://api-gestion-bovina.onrender.com"

    /**
     * Se instancia el servicio de la API una sola vez
     * (lazy) significa que se crea solo cuando se usa por primera vez
     */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON
            .build()
            .create(ApiService::class.java) // Implementa la interfaz ApiService
    }
}