package com.reyesclavero.recordatorios.data.preferencias

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.reyesclavero.recordatorios.domain.model.OrdenLista
import com.reyesclavero.recordatorios.domain.model.PreferenciasUsuario
import com.reyesclavero.recordatorios.domain.model.Tema
import com.reyesclavero.recordatorios.domain.repository.PreferenciasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// UNA sola instancia de DataStore por archivo: crear dos para el mismo archivo produce errores
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_usuario")

// CÓMO se guardan las preferencias: con DataStore Preferences (clave → valor)
class PreferenciasRepositoryDataStore(
    private val context: Context
) : PreferenciasRepository {

    // Claves tipadas: cada una sabe qué tipo de dato guarda
    private object Claves {
        val TEMA = stringPreferencesKey("tema")
        val MOSTRAR_COMPLETADOS = booleanPreferencesKey("mostrar_completados")
        val ORDEN = stringPreferencesKey("orden")
    }

    override val preferencias: Flow<PreferenciasUsuario> = context.dataStore.data
        // Si el archivo no se puede leer, se usan los valores por defecto en vez de cerrar la app
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { guardadas ->
            val porDefecto = PreferenciasUsuario()
            PreferenciasUsuario(
                // Si no hay valor guardado, o el texto no corresponde a un valor válido → por defecto
                tema = guardadas[Claves.TEMA]
                    ?.let { runCatching { Tema.valueOf(it) }.getOrNull() }
                    ?: porDefecto.tema,
                mostrarCompletados = guardadas[Claves.MOSTRAR_COMPLETADOS]
                    ?: porDefecto.mostrarCompletados,
                orden = guardadas[Claves.ORDEN]
                    ?.let { runCatching { OrdenLista.valueOf(it) }.getOrNull() }
                    ?: porDefecto.orden
            )
        }

    // edit {} guarda de forma asíncrona y segura (no bloquea la interfaz)
    override suspend fun guardarTema(tema: Tema) {
        context.dataStore.edit { it[Claves.TEMA] = tema.name }
    }

    override suspend fun guardarMostrarCompletados(mostrar: Boolean) {
        context.dataStore.edit { it[Claves.MOSTRAR_COMPLETADOS] = mostrar }
    }

    override suspend fun guardarOrden(orden: OrdenLista) {
        context.dataStore.edit { it[Claves.ORDEN] = orden.name }
    }
}


