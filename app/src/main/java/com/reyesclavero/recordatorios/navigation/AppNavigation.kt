package com.reyesclavero.recordatorios.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.reyesclavero.recordatorios.ui.ajustes.AjustesScreen
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
        startDestination = ListaRoute
    ) {
        composable<ListaRoute> {
            ListaScreen(
                onCrear = { navController.navigate(CrearRoute) },
                onAbrirDetalle = { id -> navController.navigate(DetalleRoute(id)) },
                onAbrirAjustes = { navController.navigate(AjustesRoute) }          // ← NUEVO
            )
        }

        composable<CrearRoute> {
            CrearScreen(
                onVolver = dropUnlessResumed { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable<DetalleRoute> { backStackEntry ->
            val ruta = backStackEntry.toRoute<DetalleRoute>()
            DetalleScreen(
                recordatorioId = ruta.recordatorioId,
                onVolver = dropUnlessResumed { navController.popBackStack() }
            )
        }

        composable<AjustesRoute> {                                                  // ← NUEVO
            AjustesScreen(
                onVolver = dropUnlessResumed { navController.popBackStack() }
            )
        }
    }
}