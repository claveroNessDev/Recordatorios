package com.reyesclavero.recordatorios.data

// Modos de carga del listado: valores fijos sin datos propios → enum
enum class ModoCarga { NORMAL, VACIO, ERROR }

// ⚙️ Cambia estos valores y vuelve a ejecutar para comprobar cada estado de la interfaz
object ConfiguracionSimulacion {
    val modoCarga
    = ModoCarga.NORMAL   // NORMAL | VACIO | ERROR
    val fallarGuardado = false           // true → el guardado termina en error
}