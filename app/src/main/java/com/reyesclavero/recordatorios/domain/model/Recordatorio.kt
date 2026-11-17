package com.reyesclavero.recordatorios.domain.model

import java.time.LocalDate

// Entidad principal. Los datos opcionales son anulables (?) y por defecto valen null
data class Recordatorio(
    val id: Long,
    val titulo: String,                    // obligatorio
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio,
    val descripcion: String? = null,       // opcional
    val fechaLimite: LocalDate? = null,    // opcional
    val ubicacion: String? = null          // opcional: se usará en módulos siguientes
)