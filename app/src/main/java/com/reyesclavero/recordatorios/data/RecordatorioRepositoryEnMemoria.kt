package com.reyesclavero.recordatorios.data

import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.Recordatorio
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

// CÓMO: resuelve las operaciones con datos en memoria y demoras simuladas
class RecordatorioRepositoryEnMemoria(
    modoCarga: ModoCarga,
    private val fallarGuardado: Boolean
) : RecordatorioRepository {

    // Modificable SOLO aquí adentro (private)...
    private val datos = MutableStateFlow(
        if (modoCarga == ModoCarga.VACIO) emptyList()
        else DatosSimulados.recordatoriosValidos
    )

    // ...y hacia afuera, de solo lectura
    override val recordatorios: StateFlow<List<Recordatorio>> = datos.asStateFlow()

    override val categorias: List<Categoria> = DatosSimulados.categorias.toList()

    // En modo ERROR falla solo el primer intento, para comprobar que el reintento funciona
    private var debeFallarCarga = modoCarga == ModoCarga.ERROR

    override suspend fun cargar() {
        delay(1_200)   // simula red o base de datos, sin bloquear la interfaz
        if (debeFallarCarga) {
            debeFallarCarga = false
            throw IOException("Error de conexión simulado")
        }
    }

    override suspend fun obtenerPorId(id: Long): Recordatorio? {
        delay(500)
        return datos.value.find { it.id == id }   // null si no existe
    }

    override suspend fun guardar(recordatorio: Recordatorio): Recordatorio {
        delay(1_500)
        if (fallarGuardado) throw IOException("Error simulado al guardar")

        val nuevoId = (datos.value.maxOfOrNull { it.id } ?: 0L) + 1
        val guardado = recordatorio.copy(id = nuevoId)

        // update + "lista + elemento" → crea una lista NUEVA (inmutabilidad)
        datos.update { listaActual -> listaActual + guardado }
        return guardado
    }
}