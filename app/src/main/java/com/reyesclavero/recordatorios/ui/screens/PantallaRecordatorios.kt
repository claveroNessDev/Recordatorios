package com.reyesclavero.recordatorios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reyesclavero.recordatorios.data.DatosSimulados
import com.reyesclavero.recordatorios.domain.filtros.filtrarPendientes
import com.reyesclavero.recordatorios.domain.filtros.filtrarPorPrioridad
import com.reyesclavero.recordatorios.domain.filtros.ordenarPorPrioridad
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.ui.components.TarjetaRecordatorio
import com.reyesclavero.recordatorios.ui.theme.RecordatoriosTheme

// Filtros de la pantalla: valores fijos SIN datos propios → aquí un enum basta
enum class FiltroLista(val etiqueta: String) {
    TODOS("Todos"),
    PENDIENTES("Pendientes"),
    ALTA("Alta prioridad")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios(modifier: Modifier = Modifier) {
    // ESTADO simple: el filtro elegido. rememberSaveable lo conserva al girar el teléfono
    var filtro by rememberSaveable { mutableStateOf(FiltroLista.TODOS) }

    val validos = DatosSimulados.recordatoriosValidos
    val descartados = DatosSimulados.recordatorios.size - validos.size

    // La lista visible se DERIVA del estado: cambia el filtro → Compose redibuja
    val visibles = when (filtro) {
        FiltroLista.TODOS -> ordenarPorPrioridad(validos)
        FiltroLista.PENDIENTES -> ordenarPorPrioridad(filtrarPendientes(validos))
        FiltroLista.ALTA -> filtrarPorPrioridad(validos, Prioridad.Alta)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Mis recordatorios",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "${validos.size} recordatorios · ${DatosSimulados.categorias.size} categorías · " +
                    "$descartados descartado(s) por validación",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Filtros: al tocar uno cambia el estado
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FiltroLista.entries.forEach { opcion ->
                FilterChip(
                    selected = filtro == opcion,
                    onClick = { filtro = opcion },
                    label = { Text(opcion.etiqueta) }
                )
            }
        }

        if (visibles.isEmpty()) {
            Text(text = "No hay recordatorios para este filtro")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(visibles, key = { it.id }) { recordatorio ->
                    TarjetaRecordatorio(recordatorio = recordatorio)
                }
            }
        }
    }
}

// Vista previa en Android Studio, con el tema de la app
@Preview(showBackground = true)
@Composable
fun PantallaRecordatoriosPreview() {
    RecordatoriosTheme {
        PantallaRecordatorios()
    }
}