package com.reyesclavero.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.reyesclavero.recordatorios.navigation.AppNavigation
import com.reyesclavero.recordatorios.ui.theme.RecordatoriosTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosTheme {
                AppNavigation()
            }
        }
    }
}


