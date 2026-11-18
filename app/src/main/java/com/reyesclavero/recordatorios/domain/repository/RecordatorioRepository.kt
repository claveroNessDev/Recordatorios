package com.reyesclavero.recordatorios.domain.repository

import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.Recordatorio
import kotlinx.coroutines.flow.StateFlow

// QUÉ operaciones existen. No dice CÓMO se resuelven.
interface RecordatorioRepository {

    // Lista observable: cuando cambia (por ejemplo, al guardar), quien la observa se entera
    val recordatorios: StateFlow<List<Recordatorio>>

    // Opciones válidas para el formulario
    val categorias: List<Categoria>

    // Carga inicial (puede tardar o fallar)
    suspend fun cargar()

    suspend fun obtenerPorId(id: Long): Recordatorio?

    // Devuelve el recordatorio guardado, con su id asignado
    suspend fun guardar(recordatorio: Recordatorio): Recordatorio
}