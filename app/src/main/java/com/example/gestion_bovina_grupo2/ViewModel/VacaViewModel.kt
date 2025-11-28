package com.example.gestion_bovina_grupo2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestion_bovina_grupo2.repository.ContadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VacaViewModel(
    private val contadorRepo: ContadorRepository
) : ViewModel() {


    // Contadores (persistentes)
    private val _totalVacas = MutableStateFlow(0)

    private val _registradasHoy = MutableStateFlow(0)
    val registradasHoy: StateFlow<Int> = _registradasHoy

    init {
        // Cargar contadores desde DataStore al iniciar el VM
        viewModelScope.launch {
            val c = contadorRepo.getCountersOnce()
            _totalVacas.value = c.total
            _registradasHoy.value = c.hoy
        }
    }

    // Factory para crear el ViewModel con el repo
    companion object {
        fun provideFactory(repo: ContadorRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return VacaViewModel(repo) as T
                }
            }
    }


}
