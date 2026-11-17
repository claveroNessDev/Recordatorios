package com.reyesclavero.recordatorios.domain.model

import java.time.LocalDate

// Cada estado lleva SOLO los datos que le corresponden
sealed class EstadoRecordatorio {
    data object Pendiente : EstadoRecordatorio()
    data class Completado(val fecha: LocalDate) : EstadoRecordatorio()
    data class Pospuesto(val hasta: LocalDate) : EstadoRecordatorio()
}