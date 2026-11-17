package com.reyesclavero.recordatorios.data

import com.reyesclavero.recordatorios.domain.model.Categoria
import com.reyesclavero.recordatorios.domain.model.EstadoRecordatorio
import com.reyesclavero.recordatorios.domain.model.Prioridad
import com.reyesclavero.recordatorios.domain.model.Recordatorio
import com.reyesclavero.recordatorios.domain.validation.esValido
import java.time.LocalDate

// "object" = un único contenedor para los datos de prueba (no se crean instancias)
object DatosSimulados {

    // Categorías base (private: solo se usan dentro de este archivo)
    private val salud = Categoria(1, "Salud")
    private val trabajo = Categoria(2, "Trabajo")
    private val hogar = Categoria(3, "Hogar")
    private val estudio = Categoria(4, "Estudio")

    // LIST: recordatorios en orden, de solo lectura
    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Pedir hora al dentista",
            categoria = salud,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Pendiente,
            fechaLimite = LocalDate.of(2026, 11, 20),
            ubicacion = "Clínica dental"
        ),
        Recordatorio(
            id = 2,
            titulo = "Enviar informe semanal",
            categoria = trabajo,
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Completado(LocalDate.of(2026, 11, 14)),
            descripcion = "Incluir las métricas de la semana"
        ),
        Recordatorio(
            id = 3,
            titulo = "Comprar detergente",
            categoria = hogar,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Pendiente
            // sin descripción, fecha ni ubicación: usan su valor por defecto (null)
        ),
        Recordatorio(
            id = 4,
            titulo = "Entregar Actividad 1",
            categoria = estudio,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Pendiente,
            descripcion = "PDF con capturas y enlace al repositorio",
            fechaLimite = LocalDate.of(2026, 11, 18)
        ),
        Recordatorio(
            id = 5,
            titulo = "Renovar receta médica",
            categoria = Categoria(1, "Salud"),   // creada de nuevo: mismo contenido que "salud"
            prioridad = Prioridad.Media,
            estado = EstadoRecordatorio.Pospuesto(LocalDate.of(2026, 11, 25))
        ),
        Recordatorio(
            id = 6,
            titulo = "Pagar cuentas del mes",
            categoria = hogar,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Completado(LocalDate.of(2026, 11, 10))
        ),
        Recordatorio(
            id = 7,
            titulo = "Ok",   // inválido a propósito: menos de 3 caracteres
            categoria = trabajo,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Pendiente
        )
    )


    // SET: categorías sin repetir, obtenidas de los recordatorios
    val categorias: Set<Categoria> = recordatorios.map { it.categoria }.toSet()

    // Solo los recordatorios que pasan la validación: estos son los que se muestran
    val recordatoriosValidos: List<Recordatorio> = recordatorios.filter { esValido(it) }

}

