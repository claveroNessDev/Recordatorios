package com.reyesclavero.recordatorios.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.reyesclavero.recordatorios.ui.crear.CrearScreen
import com.reyesclavero.recordatorios.ui.detalle.DetalleScreen
import com.reyesclavero.recordatorios.ui.lista.ListaScreen

// Grafo de navegación: el ÚNICO lugar que conoce el NavController.
// Las pantallas solo reciben lambdas.
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListaRoute          // destino inicial: el listado
    ) {
        composable<ListaRoute> {
            ListaScreen(
                onCrear = { navController.navigate(CrearRoute) },
                onAbrirDetalle = { id -> navController.navigate(DetalleRoute(id)) }   // solo el id
            )
        }

        composable<CrearRoute> {
            CrearScreen(
                // dropUnlessResumed: ignora un segundo toque rápido mientras ya se está saliendo
                onVolver = dropUnlessResumed { navController.popBackStack() },
                // Después de guardar se VUELVE (popBackStack), no se navega de nuevo al listado
                onGuardado = { navController.popBackStack() }
            )
        }

        composable<DetalleRoute> { backStackEntry ->
            // Recupera el argumento tipado de la ruta
            val ruta = backStackEntry.toRoute<DetalleRoute>()
            DetalleScreen(
                recordatorioId = ruta.recordatorioId,
                onVolver = dropUnlessResumed { navController.popBackStack() }
            )
        }
    }
}