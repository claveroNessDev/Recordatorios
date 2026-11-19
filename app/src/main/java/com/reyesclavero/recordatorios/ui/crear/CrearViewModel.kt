package com.reyesclavero.recordatorios.ui.crear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyesclavero.recordatorios.di.ContenedorApp
import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.EstadoRecordatorio
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.model.Recordatorio
import com.reyesclavero.recordatorios.domain.repository.RecordatorioRepository
import com.reyesclavero.recordatorios.domain.validation.validarFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class CrearViewModel(
    private val repositorio: RecordatorioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        validado(CrearUiState(categoriasDisponibles = repositorio.categorias))
    )
    val uiState: StateFlow<CrearUiState> = _uiState.asStateFlow()

    // Un evento por campo: la pantalla solo AVISA, el ViewModel decide
    fun onTituloChange(valor: String) = actualizar(Campo.TITULO) { it.copy(titulo = valor) }
    fun onDescripcionChange(valor: String) = actualizar(Campo.DESCRIPCION) { it.copy(descripcion = valor) }
    fun onCategoriaChange(valor: Categoria) = actualizar(Campo.CATEGORIA) { it.copy(categoria = valor) }
    fun onPrioridadChange(valor: Prioridad) = actualizar(Campo.PRIORIDAD) { it.copy(prioridad = valor) }

    // Aplica el cambio, marca el campo como tocado y vuelve a validar, en UNA actualización
    private fun actualizar(campo: Campo, cambio: (CrearUiState) -> CrearUiState) {
        _uiState.update { actual ->
            validado(
                cambio(actual).copy(
                    tocados = actual.tocados + campo,
                    errorGeneral = null
                )
            )
        }
    }

    private fun validado(estado: CrearUiState): CrearUiState =
        estado.copy(
            errores = validarFormulario(
                titulo = estado.titulo,
                descripcion = estado.descripcion,
                categoria = estado.categoria,
                prioridad = estado.prioridad,
                categoriasValidas = estado.categoriasDisponibles
            )
        )

    fun guardar() {
        val actual = _uiState.value
        // Sin "!!": si falta algo, simplemente no guarda
        val categoria = actual.categoria ?: return
        val prioridad = actual.prioridad ?: return
        if (!actual.puedeGuardar) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, errorGeneral = null) }
            try {
                repositorio.guardar(
                    Recordatorio(
                        id = 0,   // el repositorio asigna el id real
                        titulo = actual.titulo.trim(),
                        categoria = categoria,
                        prioridad = prioridad,
                        estado = EstadoRecordatorio.Pendiente,
                        descripcion = actual.descripcion.trim().ifBlank { null }
                    )
                )
                _uiState.update { it.copy(guardando = false, guardadoExitoso = true) }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = "No se pudo guardar el recordatorio. Revisa tu conexión e inténtalo de nuevo."
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { CrearViewModel(ContenedorApp.repositorio) }
        }
    }
}

