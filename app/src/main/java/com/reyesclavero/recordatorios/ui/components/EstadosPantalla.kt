package com.reyesclavero.recordatorios.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Base común: contenido centrado en la pantalla
@Composable
private fun EstadoCentrado(
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = contenido
        )
    }
}

@Composable
fun EstadoCarga(modifier: Modifier = Modifier) {
    EstadoCentrado(modifier) {
        CircularProgressIndicator()
        Text("Cargando recordatorios…", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun EstadoVacio(onCrear: () -> Unit, modifier: Modifier = Modifier) {
    EstadoCentrado(modifier) {
        Text("Aún no tienes recordatorios", style = MaterialTheme.typography.titleMedium)
        Text(
            "Crea el primero para empezar a organizarte.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Button(onClick = onCrear) { Text("Crear recordatorio") }
    }
}

// El error se comunica con TEXTO + acción, no solo con color
@Composable
fun EstadoError(mensaje: String, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    EstadoCentrado(modifier) {
        Text(
            "Algo salió mal",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}