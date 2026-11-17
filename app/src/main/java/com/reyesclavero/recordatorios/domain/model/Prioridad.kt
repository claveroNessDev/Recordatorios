package com.reyesclavero.recordatorios.domain.model

// Menú cerrado de prioridades. Cada una tiene un nivel (para ordenar) y una etiqueta (para mostrar)
sealed class Prioridad(val nivel: Int, val etiqueta: String) {
    data object Baja : Prioridad(1, "Baja")
    data object Media : Prioridad(2, "Media")
    data object Alta : Prioridad(3, "Alta")
}