package com.reyesclavero.recordatorios

import android.app.Application
import com.reyesclavero.recordatorios.di.ContenedorApp

// Se crea UNA vez, al iniciar la app, antes que cualquier pantalla
class RecordatoriosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ContenedorApp.inicializar(this)
    }
}



