package com.reyesclavero.recordatorios.domain.validation

import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.Prioridad

// Un mensaje por campo; null = el campo es válido
data class ErroresFormulario(
    val titulo: String? = null,
    val descripcion: String? = null,
    val categoria: String? = null,
    val prioridad: String? = null
) {
    val hayErrores: Boolean
        get() = listOfNotNull(titulo, descripcion, categoria, prioridad).isNotEmpty()
}

// Valida lo que el usuario ESCRIBIÓ y SELECCIONÓ (todavía no es un Recordatorio)
fun validarFormulario(
    titulo: String,
    descripcion: String,
    categoria: Categoria?,          // null = no seleccionada
    prioridad: Prioridad?,          // null = no seleccionada
    categoriasValidas: List<Categoria>
): ErroresFormulario {
    val tituloLimpio = titulo.trim()
    return ErroresFormulario(
        titulo = when {
            tituloLimpio.isEmpty() -> "El título es obligatorio"
            tituloLimpio.length < TITULO_MIN -> "El título debe tener al menos $TITULO_MIN caracteres"
            tituloLimpio.length > TITULO_MAX -> "El título no puede superar los $TITULO_MAX caracteres"
            else -> null
        },
        descripcion = if (descripcion.length > DESCRIPCION_MAX) {
            "La descripción no puede superar los $DESCRIPCION_MAX caracteres"
        } else null,
        categoria = when {
            categoria == null -> "Selecciona una categoría"
            categoria !in categoriasValidas -> "La categoría no pertenece a las opciones definidas"
            else -> null
        },
        prioridad = if (prioridad == null) "Selecciona una prioridad" else null
    )
}