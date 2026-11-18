package com.reyesclavero.recordatorios.di

import com.reyesclavero.recordatorios.data.ConfiguracionSimulacion
import com.reyesclavero.recordatorios.data.RecordatorioRepositoryEnMemoria
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository

// Inyección de dependencias manual: crea UNA instancia del repositorio
// para que listado, creación y detalle compartan los mismos datos
object ContenedorApp {
    val repositorio: RecordatorioRepository = RecordatorioRepositoryEnMemoria(
        modoCarga = ConfiguracionSimulacion.modoCarga,
        fallarGuardado = ConfiguracionSimulacion.fallarGuardado
    )
}