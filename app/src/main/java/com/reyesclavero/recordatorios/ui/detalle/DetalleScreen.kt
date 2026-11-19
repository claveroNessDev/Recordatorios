package com.reyesclavero.recordatorios.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyesclavero.recordatorios.ui.components.EstadoCarga
import com.reyesclavero.recordatorios.ui.components.colorDeCategoria
import com.reyesclavero.recordatorios.ui.components.textoEstado
import java.time.format.DateTimeFormatter

private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

// CON ESTADO: recibe solo el id y crea su ViewModel con ese id
@Composable
fun DetalleScreen(
    recordatorioId: Long,
    onVolver: () -> Unit,
    viewModel: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(recordatorioId))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetalleContenido(uiState = uiState, onVolver = onVolver)
}

// SIN ESTADO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContenido(uiState: DetalleUiState, onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (uiState) {
            DetalleUiState.Cargando -> EstadoCarga(modifier)

            DetalleUiState.NoEncontrado -> Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No encontramos este recordatorio", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onVolver) { Text("Volver al listado") }
            }

            is DetalleUiState.Encontrado -> {
                val recordatorio = uiState.recordatorio
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(recordatorio.titulo, style = MaterialTheme.typography.headlineSmall)

                    FilaDetalle("Categoría") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(colorDeCategoria(recordatorio.categoria), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(recordatorio.categoria.nombre)
                        }
                    }
                    FilaDetalle("Prioridad") { Text(recordatorio.prioridad.etiqueta) }
                    FilaDetalle("Estado") { Text(textoEstado(recordatorio.estado)) }
                    FilaDetalle("Descripción") { Text(recordatorio.descripcion ?: "Sin descripción") }
                    FilaDetalle("Fecha límite") {
                        Text(recordatorio.fechaLimite?.format(formatoFecha) ?: "Sin fecha límite")
                    }
                    recordatorio.ubicacion?.let { ubicacion ->
                        FilaDetalle("Ubicación") { Text(ubicacion) }
                    }
                }
            }
        }
    }
}

// Etiqueta + valor; mergeDescendants hace que el lector de pantalla los lea juntos
@Composable
private fun FilaDetalle(etiqueta: String, contenido: @Composable () -> Unit) {
    Column(
        modifier = Modifier.semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        contenido()
    }
}

