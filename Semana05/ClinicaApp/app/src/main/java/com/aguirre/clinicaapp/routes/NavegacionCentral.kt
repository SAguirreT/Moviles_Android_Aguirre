package com.aguirre.clinicaapp.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.aguirre.clinicaapp.data.ProviderClinico
import com.aguirre.clinicaapp.views.AccesoView
import com.aguirre.clinicaapp.views.PanelPrincipalView
import com.aguirre.clinicaapp.views.DirectorioMedicosView
import com.aguirre.clinicaapp.views.FichaMedicoView
import com.aguirre.clinicaapp.views.PerfilUsuarioView

sealed class Destinos(val ruta: String) {
    object VistaAcceso : Destinos("login_view")
    object VistaPanel : Destinos("panel_view")
    object VistaDirectorio : Destinos("directorio_view")
    object VistaFichaMedico : Destinos("ficha_view/{idDoc}") {
        fun armarRuta(idDoc: Int) = "ficha_view/$idDoc"
    }
    object VistaPerfilUsuario : Destinos("perfil_view")
}

@Composable
fun GestorNavegacion() {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Destinos.VistaAcceso.ruta) {
        composable(Destinos.VistaAcceso.ruta) { AccesoView(navHostController) }
        composable(Destinos.VistaPanel.ruta) { PanelPrincipalView(navHostController) }
        composable(Destinos.VistaDirectorio.ruta) { DirectorioMedicosView(navHostController) }
        composable(
            route = Destinos.VistaFichaMedico.ruta,
            arguments = listOf(navArgument("idDoc") { type = NavType.IntType })
        ) { entry ->
            val idDoc = entry.arguments?.getInt("idDoc") ?: 1
            val doctorObtenido = ProviderClinico.listaMedicos.find { it.idMedico == idDoc } ?: ProviderClinico.listaMedicos.first()
            FichaMedicoView(navHostController, doctorObtenido)
        }
        composable(Destinos.VistaPerfilUsuario.ruta) { PerfilUsuarioView(navHostController) }
    }
}