package com.reyesclavero.recordatorios.domain.validation

import com.reyesclavero.recordatorios.domain.model.Recordatorio

// Resultado cerrado: o es válido, o es inválido con su lista de motivos
sealed class ResultadoValidacion {
    data object Valido : ResultadoValidacion()
    data class Invalido(val errores: List<String>) : ResultadoValidacion()
}

// Reglas del producto en un solo lugar: si cambian, se modifican aquí
const val TITULO_MIN = 3
const val TITULO_MAX = 50
const val DESCRIPCION_MAX = 200

fun validarRecordatorio(recordatorio: Recordatorio): ResultadoValidacion {
    // Mutable SOLO dentro de la función, para ir acumulando errores
    val errores = mutableListOf<String>()

    // Regla 1: título obligatorio y con largo válido (trim() quita espacios al inicio y al final)
    val titulo = recordatorio.titulo.trim()
    when {
        titulo.isEmpty() -> errores.add("El título es obligatorio")
        titulo.length < TITULO_MIN -> errores.add("El título debe tener al menos $TITULO_MIN caracteres")
        titulo.length > TITULO_MAX -> errores.add("El título no puede superar los $TITULO_MAX caracteres")
    }

    // Regla 2: la descripción es OPCIONAL → solo se valida si existe
    recordatorio.descripcion?.let { descripcion ->
        if (descripcion.length > DESCRIPCION_MAX) {
            errores.add("La descripción no puede superar los $DESCRIPCION_MAX caracteres")
        }
    }

    return if (errores.isEmpty()) {
        ResultadoValidacion.Valido
    } else {
        ResultadoValidacion.Invalido(errores.toList())   // toList(): entrega una copia de solo lectura
    }
}

// Atajo para filtrar listas: true si el recordatorio pasa todas las reglas
fun esValido(recordatorio: Recordatorio): Boolean =
    validarRecordatorio(recordatorio) is ResultadoValidacion.Valido