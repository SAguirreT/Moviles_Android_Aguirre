package com.tecsup.laptops.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.laptops.ui.admin.AdminScreen
import com.tecsup.laptops.ui.detalle.DetalleScreen
import com.tecsup.laptops.ui.favoritos.FavoritosScreen
import com.tecsup.laptops.ui.lista.ListaScreen
import com.tecsup.laptops.ui.login.LoginScreen

sealed class Pantalla(val ruta: String) {
    data object Login : Pantalla("login")
    data object Lista : Pantalla("lista")
    data object Detalle : Pantalla("detalle/{id}") {
        fun crearRuta(id: Int) = "detalle/$id"
    }
    data object Favoritos : Pantalla("favoritos")
    data object Admin : Pantalla("admin")
}

@Composable
fun AppNav(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {
        composable(Pantalla.Login.ruta) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Pantalla.Lista.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Lista.ruta) {
            ListaScreen(
                onLaptopClick = { id ->
                    navController.navigate(Pantalla.Detalle.crearRuta(id))
                },
                onIrAFavoritos = {
                    navController.navigate(Pantalla.Favoritos.ruta)
                },
                onIrAAdmin = {
                    navController.navigate(Pantalla.Admin.ruta)
                }
            )
        }
        composable(Pantalla.Detalle.ruta) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleScreen(
                laptopId = id,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Pantalla.Favoritos.ruta) {
            FavoritosScreen(
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Pantalla.Admin.ruta) {
            AdminScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}