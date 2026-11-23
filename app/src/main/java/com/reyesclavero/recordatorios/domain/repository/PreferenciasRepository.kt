package com.reyesclavero.recordatorios.domain.repository

import com.reyesclavero.recordatorios.domain.model.OrdenLista
import com.reyesclavero.recordatorios.domain.model.PreferenciasUsuario
import com.reyesclavero.recordatorios.domain.model.Tema
import kotlinx.coroutines.flow.Flow

// QUÉ se puede hacer con las preferencias (no dice que se use DataStore)
interface PreferenciasRepository {

    // Flujo observable: cada vez que una preferencia cambia, quien escucha recibe el valor nuevo
    val preferencias: Flow<PreferenciasUsuario>

    suspend fun guardarTema(tema: Tema)
    suspend fun guardarMostrarCompletados(mostrar: Boolean)
    suspend fun guardarOrden(orden: OrdenLista)
}

