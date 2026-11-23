package com.reyesclavero.recordatorios.di

import android.content.Context
import com.reyesclavero.recordatorios.data.ConfiguracionSimulacion
import com.reyesclavero.recordatorios.data.RecordatorioRepositoryEnMemoria
import com.reyesclavero.recordatorios.data.preferencias.PreferenciasRepositoryDataStore
import com.reyesclavero.recordatorios.domain.repository.PreferenciasRepository
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository

// Inyección de dependencias manual: crea UNA instancia de cada repositorio
object ContenedorApp {

    private lateinit var appContext: Context

    // Se llama UNA vez al iniciar la app (desde RecordatoriosApp, en el paso siguiente)
    fun inicializar(context: Context) {
        appContext = context.applicationContext   // el de la app completa, no el de una pantalla
    }

    // Por ahora sigue en memoria; mañana lo reemplazamos por la versión con Room
    val repositorio: RecordatorioRepository = RecordatorioRepositoryEnMemoria(
        modoCarga = ConfiguracionSimulacion.modoCarga,
        fallarGuardado = ConfiguracionSimulacion.fallarGuardado
    )

    // "by lazy": se crea la PRIMERA vez que alguien lo usa, cuando el Context ya existe
    val preferencias: PreferenciasRepository by lazy {
        PreferenciasRepositoryDataStore(appContext)
    }
}

