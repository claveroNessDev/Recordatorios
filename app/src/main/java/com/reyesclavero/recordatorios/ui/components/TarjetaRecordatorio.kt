package com.reyesclavero.recordatorios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyesclavero.recordatorios.domain.model.EstadoRecordatorio
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.model.Recordatorio
import java.time.format.DateTimeFormatter

private val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

// when EXHAUSTIVO sobre la sealed class: si agregas un estado nuevo, el compilador te avisa aquí
fun textoEstado(estado: EstadoRecordatorio): String = when (estado) {
    is EstadoRecordatorio.Pendiente -> "Pendiente"
    is EstadoRecordatorio.Completado -> "Completado el ${estado.fecha.format(formatoFecha)}"
    is EstadoRecordatorio.Pospuesto -> "Pospuesto hasta ${estado.hasta.format(formatoFecha)}"
}

// Colores tomados del tema Material 3 (no colores sueltos)
@Composable
private fun colorDePrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.Alta -> MaterialTheme.colorScheme.error
    Prioridad.Media -> MaterialTheme.colorScheme.tertiary
    Prioridad.Baja -> MaterialTheme.colorScheme.outline
}

// Componente reutilizable: recibe un Modifier para que la pantalla decida cómo ubicarlo
@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Fila 1: título + prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = recordatorio.prioridad.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    color = colorDePrioridad(recordatorio.prioridad)
                )
            }

            // Fila 2: categoría con su color (sale del Map)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(colorDeCategoria(recordatorio.categoria), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = recordatorio.categoria.nombre,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Fila 3: estado
            Text(
                text = textoEstado(recordatorio.estado),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Datos OPCIONALES: solo se muestran si existen (null safety)
            recordatorio.descripcion?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
            recordatorio.fechaLimite?.let {
                Text(
                    text = "Fecha límite: ${it.format(formatoFecha)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}