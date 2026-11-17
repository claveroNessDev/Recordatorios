package com.reyesclavero.recordatorios.domain.model

// Entidad simple: data class para que dos categorías iguales se reconozcan como iguales
data class Categoria(
    val id: Int,
    val nombre: String
)