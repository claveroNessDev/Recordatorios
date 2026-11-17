package com.reyesclavero.recordatorios.ui.components

import androidx.compose.ui.graphics.Color
import com.reyesclavero.recordatorios.domain.model.Categoria

// MAP: cada categoría (por nombre) asociada a un color.
// Vive en la capa UI porque el color es una decisión visual, no de dominio.
val coloresPorCategoria: Map<String, Color> = mapOf(
    "Salud" to Color(0xFF2E7D32),     // verde
    "Trabajo" to Color(0xFF1565C0),   // azul
    "Hogar" to Color(0xFFEF6C00),     // naranjo
    "Estudio" to Color(0xFF6A1B9A)    // morado
)

// Map[clave] devuelve null si la clave no existe → Elvis (?:) da un color por defecto
fun colorDeCategoria(categoria: Categoria): Color =
    coloresPorCategoria[categoria.nombre] ?: Color.Gray