package com.tuapp.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuapp.navlab.screens.*

@Composable
fun AppNavigation() {
    // Inicialización del gestor de estados de navegación
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Define la pantalla inicial al abrir la aplicación
        startDestination = Screen.Home.route
    ) {
        // Mapeo hacia el composable HomeScreen
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // Mapeo hacia el composable ListScreen
        composable(Screen.List.route) {
            ListScreen(navController)
        }

        // Mapeo hacia el composable ProfileScreen
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Mapeo con recepción de argumento de tipo entero
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(name = "itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            // Recupera el ID enviado o asigna valor por defecto
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(navController, itemId)
        }
    }
}