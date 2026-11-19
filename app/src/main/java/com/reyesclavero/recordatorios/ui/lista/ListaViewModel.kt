package com.reyesclavero.recordatorios.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyesclavero.recordatorios.di.ContenedorApp
import com.reyesclavero.recordatorios.domain.filtros.ordenarPorPrioridad
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// Recibe la INTERFAZ: no sabe si los datos vienen de memoria, Room o una API
class ListaViewModel(
    private val repositorio: RecordatorioRepository
) : ViewModel() {

    // Mutable por dentro (privado)...
    private val _uiState = MutableStateFlow<ListaUiState>(ListaUiState.Cargando)
    // ...solo lectura hacia la pantalla
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    private var trabajoCarga: Job? = null

    init {
        cargar()   // carga apenas se crea el ViewModel
    }

    // Pública: también la usa el botón "Reintentar"
    fun cargar() {
        trabajoCarga?.cancel()   // evita dos cargas simultáneas
        trabajoCarga = viewModelScope.launch {
            _uiState.value = ListaUiState.Cargando
            try {
                repositorio.cargar()
            } catch (e: IOException) {
                _uiState.value = ListaUiState.Error(
                    "No pudimos cargar tus recordatorios. Revisa tu conexión e inténtalo de nuevo."
                )
                return@launch
            }
            // Observa la lista: si se guarda un recordatorio nuevo, el listado se actualiza solo
            repositorio.recordatorios.collect { lista ->
                _uiState.value = if (lista.isEmpty()) {
                    ListaUiState.Vacio
                } else {
                    ListaUiState.ConDatos(ordenarPorPrioridad(lista))
                }
            }
        }
    }

    // Fábrica: le dice a Android CÓMO crear este ViewModel con su repositorio
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ListaViewModel(ContenedorApp.repositorio) }
        }
    }
}