package com.reyesclavero.recordatorios.ui.detalle

import com.reyesclavero.recordatorios.domain.model.Recordatorio

// Los 3 estados posibles del detalle
sealed interface DetalleUiState {
    data object Cargando : DetalleUiState
    data class Encontrado(val recordatorio: Recordatorio) : DetalleUiState
    data object NoEncontrado : DetalleUiState   // el id no existe en el repositorio
}

