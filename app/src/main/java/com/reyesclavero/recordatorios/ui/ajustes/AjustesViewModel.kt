package com.reyesclavero.recordatorios.ui.ajustes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyesclavero.recordatorios.di.ContenedorApp
import com.reyesclavero.recordatorios.domain.model.OrdenLista
import com.reyesclavero.recordatorios.domain.model.PreferenciasUsuario
import com.reyesclavero.recordatorios.domain.model.Tema
import com.reyesclavero.recordatorios.domain.repository.PreferenciasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(
    private val repositorio: PreferenciasRepository
) : ViewModel() {

    // Convierte el Flow de DataStore en un StateFlow que la pantalla puede observar
    val preferencias: StateFlow<PreferenciasUsuario> = repositorio.preferencias
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),   // sigue activo 5 s tras salir (ej. al girar)
            initialValue = PreferenciasUsuario()                // valores por defecto mientras lee el archivo
        )

    // Cada cambio se guarda en DataStore; el Flow emite el valor nuevo y la pantalla se actualiza sola
    fun onTemaChange(tema: Tema) {
        viewModelScope.launch { repositorio.guardarTema(tema) }
    }

    fun onMostrarCompletadosChange(mostrar: Boolean) {
        viewModelScope.launch { repositorio.guardarMostrarCompletados(mostrar) }
    }

    fun onOrdenChange(orden: OrdenLista) {
        viewModelScope.launch { repositorio.guardarOrden(orden) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { AjustesViewModel(ContenedorApp.preferencias) }
        }
    }
}