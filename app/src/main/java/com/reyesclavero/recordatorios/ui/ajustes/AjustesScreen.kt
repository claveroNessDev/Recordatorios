package com.reyesclavero.recordatorios.ui.ajustes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyesclavero.recordatorios.domain.model.OrdenLista
import com.reyesclavero.recordatorios.domain.model.PreferenciasUsuario
import com.reyesclavero.recordatorios.domain.model.Tema

// CON ESTADO
@Composable
fun AjustesScreen(
    onVolver: () -> Unit,
    viewModel: AjustesViewModel = viewModel(factory = AjustesViewModel.Factory)
) {
    val preferencias by viewModel.preferencias.collectAsStateWithLifecycle()

    AjustesContenido(
        preferencias = preferencias,
        onVolver = onVolver,
        onTemaChange = viewModel::onTemaChange,
        onMostrarCompletadosChange = viewModel::onMostrarCompletadosChange,
        onOrdenChange = viewModel::onOrdenChange
    )
}

// SIN ESTADO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesContenido(
    preferencias: PreferenciasUsuario,
    onVolver: () -> Unit,
    onTemaChange: (Tema) -> Unit,
    onMostrarCompletadosChange: (Boolean) -> Unit,
    onOrdenChange: (OrdenLista) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // PREFERENCIA 1: tema
            GrupoRadio(
                titulo = "Tema",
                opciones = Tema.entries,
                seleccionada = preferencias.tema,
                etiqueta = ::etiquetaTema,
                onSeleccionar = onTemaChange
            )

            // PREFERENCIA 2: mostrar completados (toda la fila es tocable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = preferencias.mostrarCompletados,
                        role = Role.Switch,
                        onValueChange = onMostrarCompletadosChange
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mostrar completados", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Incluye en el listado los recordatorios ya completados",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = preferencias.mostrarCompletados, onCheckedChange = null)
            }

            // PREFERENCIA 3: orden del listado
            GrupoRadio(
                titulo = "Orden del listado",
                opciones = OrdenLista.entries,
                seleccionada = preferencias.orden,
                etiqueta = ::etiquetaOrden,
                onSeleccionar = onOrdenChange
            )
        }
    }
}

// Grupo de opciones excluyentes: el lector de pantalla lo anuncia como un grupo de radio
@Composable
private fun <T> GrupoRadio(
    titulo: String,
    opciones: List<T>,
    seleccionada: T,
    etiqueta: (T) -> String,
    onSeleccionar: (T) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        Text(titulo, style = MaterialTheme.typography.titleMedium)
        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = opcion == seleccionada,
                        onClick = { onSeleccionar(opcion) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = opcion == seleccionada, onClick = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(etiqueta(opcion))
            }
        }
    }
}

private fun etiquetaTema(tema: Tema): String = when (tema) {
    Tema.SISTEMA -> "Según el sistema"
    Tema.CLARO -> "Claro"
    Tema.OSCURO -> "Oscuro"
}

private fun etiquetaOrden(orden: OrdenLista): String = when (orden) {
    OrdenLista.PRIORIDAD -> "Por prioridad"
    OrdenLista.FECHA_LIMITE -> "Por fecha límite"
    OrdenLista.TITULO -> "Por título (A-Z)"
}


