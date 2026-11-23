package com.reyesclavero.recordatorios.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyesclavero.recordatorios.data.DatosSimulados
import com.reyesclavero.recordatorios.ui.components.EstadoCarga
import com.reyesclavero.recordatorios.ui.components.EstadoError
import com.reyesclavero.recordatorios.ui.components.EstadoVacio
import com.reyesclavero.recordatorios.ui.components.TarjetaRecordatorio
import com.reyesclavero.recordatorios.ui.theme.RecordatoriosTheme

// CON ESTADO: obtiene el ViewModel y observa su StateFlow
@Composable
fun ListaScreen(
    onCrear: () -> Unit,
    onAbrirDetalle: (Long) -> Unit,
    onAbrirAjustes: () -> Unit,                                   // ← NUEVO
    viewModel: ListaViewModel = viewModel(factory = ListaViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ListaContenido(
        uiState = uiState,
        onCrear = onCrear,
        onAbrirDetalle = onAbrirDetalle,
        onAbrirAjustes = onAbrirAjustes,                          // ← NUEVO
        onReintentar = viewModel::cargar
    )
}

// SIN ESTADO: solo recibe datos y lambdas → se puede previsualizar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContenido(
    uiState: ListaUiState,
    onCrear: () -> Unit,
    onAbrirDetalle: (Long) -> Unit,
    onAbrirAjustes: () -> Unit,                                   // ← NUEVO
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") },
                actions = {                                       // ← NUEVO: acción a la derecha
                    TextButton(onClick = onAbrirAjustes) { Text("Ajustes") }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onCrear) {
                Text("Nuevo recordatorio")
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (uiState) {
            ListaUiState.Cargando -> EstadoCarga(modifier)
            ListaUiState.Vacio -> EstadoVacio(onCrear = onCrear, modifier = modifier)
            is ListaUiState.Error -> EstadoError(
                mensaje = uiState.mensaje,
                onReintentar = onReintentar,
                modifier = modifier
            )
            is ListaUiState.ConDatos -> LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.recordatorios, key = { it.id }) { recordatorio ->
                    TarjetaRecordatorio(
                        recordatorio = recordatorio,
                        onClick = { onAbrirDetalle(recordatorio.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaContenidoPreview() {
    RecordatoriosTheme {
        ListaContenido(
            uiState = ListaUiState.ConDatos(DatosSimulados.recordatoriosValidos),
            onCrear = {},
            onAbrirDetalle = {},
            onAbrirAjustes = {},                                  // ← NUEVO
            onReintentar = {}
        )
    }
}