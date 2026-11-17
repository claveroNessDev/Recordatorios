package com.reyesclavero.recordatorios.domain.filtros

import com.reyesclavero.recordatorios.domain.model.EstadoRecordatorio
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.model.Recordatorio

// Filtra por ESTADO: solo los pendientes
fun filtrarPendientes(lista: List<Recordatorio>): List<Recordatorio> =
    lista.filter { it.estado is EstadoRecordatorio.Pendiente }

// Filtra por PRIORIDAD: la que se pida
fun filtrarPorPrioridad(lista: List<Recordatorio>, prioridad: Prioridad): List<Recordatorio> =
    lista.filter { it.prioridad == prioridad }

// Ordena de mayor a menor prioridad, usando el "nivel" que definimos en Prioridad
fun ordenarPorPrioridad(lista: List<Recordatorio>): List<Recordatorio> =
    lista.sortedByDescending { it.prioridad.nivel }