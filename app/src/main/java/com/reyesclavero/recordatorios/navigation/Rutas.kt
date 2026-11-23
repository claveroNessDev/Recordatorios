package com.reyesclavero.recordatorios.navigation

import kotlinx.serialization.Serializable

// Rutas TIPADAS: cada pantalla es un tipo de Kotlin, no un texto como "detalle/{id}"

@Serializable
data object ListaRoute                                   // destino inicial, sin argumentos

@Serializable
data object CrearRoute                                   // sin argumentos

@Serializable
data class DetalleRoute(val recordatorioId: Long)        // lleva SOLO el identificador

@Serializable
data object AjustesRoute                                 // sin argumentos