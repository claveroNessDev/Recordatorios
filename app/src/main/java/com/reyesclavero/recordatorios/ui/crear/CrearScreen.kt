package com.reyesclavero.recordatorios.ui.crear

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.validation.DESCRIPCION_MAX
import com.reyesclavero.recordatorios.domain.validation.TITULO_MAX
import com.reyesclavero.recordatorios.domain.validation.TITULO_MIN

// CON ESTADO: conecta el ViewModel con la pantalla
@Composable
fun CrearScreen(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: CrearViewModel = viewModel(factory = CrearViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Cuando el guardado termina bien, avisa UNA vez para volver al listado
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) onGuardado()
    }

    CrearContenido(
        uiState = uiState,
        onVolver = onVolver,
        onTituloChange = viewModel::onTituloChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onGuardar = viewModel::guardar
    )
}

// SIN ESTADO: campos controlados (reciben valor, avisan cambios)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearContenido(
    uiState: CrearUiState,
    onVolver: () -> Unit,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (Categoria) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onGuardar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo recordatorio") },
                navigationIcon = {
                    TextButton(onClick = onVolver, enabled = !uiState.guardando) { Text("Volver") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TÍTULO: borde de error + mensaje de texto + semántica para lectores de pantalla
            val errorTitulo = uiState.errorDe(Campo.TITULO)
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = onTituloChange,
                label = { Text("Título *") },
                singleLine = true,
                isError = errorTitulo != null,
                supportingText = {
                    Text(errorTitulo ?: "Obligatorio, entre $TITULO_MIN y $TITULO_MAX caracteres")
                },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { if (errorTitulo != null) error(errorTitulo) }
            )

            // DESCRIPCIÓN (opcional) con contador de caracteres
            val errorDescripcion = uiState.errorDe(Campo.DESCRIPCION)
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción (opcional)") },
                minLines = 3,
                isError = errorDescripcion != null,
                supportingText = {
                    Text(errorDescripcion ?: "${uiState.descripcion.length}/$DESCRIPCION_MAX")
                },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { if (errorDescripcion != null) error(errorDescripcion) }
            )

            // CATEGORÍA: solo las opciones definidas
            GrupoOpciones(
                titulo = "Categoría *",
                opciones = uiState.categoriasDisponibles,
                seleccionada = uiState.categoria,
                etiqueta = { it.nombre },
                onSeleccionar = onCategoriaChange,
                mensajeError = uiState.errorDe(Campo.CATEGORIA),
                habilitado = !uiState.guardando
            )

            // PRIORIDAD
            GrupoOpciones(
                titulo = "Prioridad *",
                opciones = listOf(Prioridad.Baja, Prioridad.Media, Prioridad.Alta),
                seleccionada = uiState.prioridad,
                etiqueta = { it.etiqueta },
                onSeleccionar = onPrioridadChange,
                mensajeError = uiState.errorDe(Campo.PRIORIDAD),
                habilitado = !uiState.guardando
            )

            // ERROR GENERAL de guardado: texto visible + anunciado por el lector de pantalla
            uiState.errorGeneral?.let { mensaje ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite }
                ) {
                    Text(
                        text = "⚠ $mensaje",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // GUARDAR: deshabilitado si es inválido o si está guardando
            Button(
                onClick = onGuardar,
                enabled = uiState.puedeGuardar,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.guardando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardando…")
                } else {
                    Text("Guardar")
                }
            }
            if (!uiState.puedeGuardar && !uiState.guardando) {
                Text(
                    text = "Completa los campos obligatorios (*) para habilitar Guardar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Grupo de opciones reutilizable: <T> permite usarlo con Categoria y con Prioridad
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> GrupoOpciones(
    titulo: String,
    opciones: List<T>,
    seleccionada: T?,
    etiqueta: (T) -> String,
    onSeleccionar: (T) -> Unit,
    mensajeError: String?,
    habilitado: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opciones.forEach { opcion ->
                FilterChip(
                    selected = opcion == seleccionada,
                    onClick = { onSeleccionar(opcion) },
                    label = { Text(etiqueta(opcion)) },
                    enabled = habilitado
                )
            }
        }
        if (mensajeError != null) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}
