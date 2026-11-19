package com.reyesclavero.recordatorios.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyesclavero.recordatorios.di.ContenedorApp
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Recibe el IDENTIFICADOR (no el objeto completo) y busca el recordatorio en el repositorio
class DetalleViewModel(
    private val recordatorioId: Long,
    private val repositorio: RecordatorioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val recordatorio = repositorio.obtenerPorId(recordatorioId)
            _uiState.value = if (recordatorio != null) {
                DetalleUiState.Encontrado(recordatorio)
            } else {
                DetalleUiState.NoEncontrado
            }
        }
    }

    // La fábrica recibe el id, porque cada detalle es de un recordatorio distinto
    companion object {
        fun factory(recordatorioId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer { DetalleViewModel(recordatorioId, ContenedorApp.repositorio) }
        }
    }
}

