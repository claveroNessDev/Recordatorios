package com.reyesclavero.recordatorios.domain.model

// Opciones de tema: valores fijos sin datos propios → enum
enum class Tema { SISTEMA, CLARO, OSCURO }

// Criterios de orden del listado
enum class OrdenLista { PRIORIDAD, FECHA_LIMITE, TITULO }

// Las 3 preferencias de la app, con sus valores PREDETERMINADOS
// (los que se usan la primera vez o si lo guardado no es válido)
data class PreferenciasUsuario(
    val tema: Tema = Tema.SISTEMA,
    val mostrarCompletados: Boolean = true,
    val orden: OrdenLista = OrdenLista.PRIORIDAD
)


