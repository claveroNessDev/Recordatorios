package com.reyesclavero.recordatorios.ui.crear

import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.validation.ErroresFormulario

// Campos del formulario (para saber cuáles ya tocó el usuario)
enum class Campo { TITULO, DESCRIPCION, CATEGORIA, PRIORIDAD }

// TODO lo que la pantalla necesita para dibujarse, en un solo objeto inmutable
data class CrearUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,                 // null = sin seleccionar
    val prioridad: Prioridad? = null,                 // null = sin seleccionar
    val categoriasDisponibles: List<Categoria> = emptyList(),
    val errores: ErroresFormulario = ErroresFormulario(),
    val tocados: Set<Campo> = emptySet(),
    val guardando: Boolean = false,
    val errorGeneral: String? = null,
    val guardadoExitoso: Boolean = false
) {
    // Guardar: solo si el formulario es válido Y no hay un guardado en curso
    val puedeGuardar: Boolean
        get() = !errores.hayErrores && !guardando

    // El error de un campo se muestra solo si el usuario ya lo modificó
    fun errorDe(campo: Campo): String? {
        if (campo !in tocados) return null
        return when (campo) {
            Campo.TITULO -> errores.titulo
            Campo.DESCRIPCION -> errores.descripcion
            Campo.CATEGORIA -> errores.categoria
            Campo.PRIORIDAD -> errores.prioridad
        }
    }
}