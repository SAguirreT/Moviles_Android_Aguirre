package com.tecsup.laptops.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.laptops.AppContainer
import com.tecsup.laptops.ui.admin.AdminScreen
import com.tecsup.laptops.ui.admin.AdminViewModel
import com.tecsup.laptops.ui.detalle.DetalleScreen
import com.tecsup.laptops.ui.detalle.DetalleViewModel
import com.tecsup.laptops.ui.favoritos.FavoritosScreen
import com.tecsup.laptops.ui.favoritos.FavoritosViewModel
import com.tecsup.laptops.ui.lista.ListaScreen
import com.tecsup.laptops.ui.lista.ListaViewModel
import com.tecsup.laptops.ui.login.LoginScreen
import com.tecsup.laptops.ui.login.LoginViewModel

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
        startDestination = Pantalla.Login.ruta
    ) {
        composable(Pantalla.Login.ruta) {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = {
                    navController.navigate(Pantalla.Lista.ruta) {
                        popUpTo(Pantalla.Login.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Lista.ruta) {
            val context = LocalContext.current
            val container = AppContainer.obtener(context)
            val viewModel: ListaViewModel = viewModel(factory = ListaViewModel.provideFactory(container.itemRepository, container.notificador))

            // Validar si el usuario actual autenticado es Administrador
            val usuarioActual = FirebaseAuth.getInstance().currentUser
            val esAdmin = usuarioActual?.email?.lowercase()?.contains("admin") == true

            ListaScreen(
                viewModel = viewModel,
                esAdmin = esAdmin, // Solo será true para administradores
                onLaptopClick = { id ->
                    navController.navigate(Pantalla.Detalle.crearRuta(id))
                },
                onIrAFavoritos = {
                    navController.navigate(Pantalla.Favoritos.ruta)
                },
                onIrAAdmin = {
                    navController.navigate(Pantalla.Admin.ruta)
                },
                onCerrarSesion = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Pantalla.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Detalle.ruta) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val context = LocalContext.current
            val container = AppContainer.obtener(context)
            val viewModel: DetalleViewModel = viewModel(factory = DetalleViewModel.provideFactory(container.itemRepository))
            DetalleScreen(
                laptopId = id,
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Pantalla.Favoritos.ruta) {
            val context = LocalContext.current
            val container = AppContainer.obtener(context)
            val viewModel: FavoritosViewModel = viewModel(factory = FavoritosViewModel.provideFactory(container.itemRepository))
            FavoritosScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Pantalla.Admin.ruta) {
            val context = LocalContext.current
            val container = AppContainer.obtener(context)
            val viewModel: AdminViewModel = viewModel(factory = AdminViewModel.provideFactory(container.itemRepository, container.notificador))
            AdminScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
