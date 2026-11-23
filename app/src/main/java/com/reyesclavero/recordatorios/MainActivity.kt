package com.reyesclavero.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyesclavero.recordatorios.di.ContenedorApp
import com.reyesclavero.recordatorios.domain.model.PreferenciasUsuario
import com.reyesclavero.recordatorios.domain.model.Tema
import com.reyesclavero.recordatorios.navigation.AppNavigation
import com.reyesclavero.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Observa las preferencias: si cambias el tema en Ajustes, toda la app se redibuja
            val preferencias by ContenedorApp.preferencias.preferencias
                .collectAsStateWithLifecycle(initialValue = PreferenciasUsuario())

            val temaOscuro = when (preferencias.tema) {
                Tema.SISTEMA -> isSystemInDarkTheme()
                Tema.CLARO -> false
                Tema.OSCURO -> true
            }

            RecordatoriosTheme(darkTheme = temaOscuro) {
                AppNavigation()
            }
        }
    }
}

