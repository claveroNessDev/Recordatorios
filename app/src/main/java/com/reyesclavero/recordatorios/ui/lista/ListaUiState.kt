package com.reyesclavero.recordatorios.ui.lista

import com.reyesclavero.recordatorios.domain.model.Recordatorio

// Los 4 estados posibles del listado: la pantalla está siempre en UNO de ellos
sealed interface ListaUiState {
    data object Cargando : ListaUiState
    data object Vacio : ListaUiState
    data class ConDatos(val recordatorios: List<Recordatorio>) : ListaUiState
    data class Error(val mensaje: String) : ListaUiState
}