package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaEditar
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = PostRepositoryRemote(RetrofitClient.api)
        setContent {
            MuroCampusTheme {
                val viewModel: PostsViewModel = viewModel(factory = PostsViewModelFactory(repository))
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "lista") {
                    composable("lista") {
                        PantallaLista(
                            viewModel = viewModel,
                            onNavigateToDetalle = { postId ->
                                viewModel.seleccionarPost(postId)
                                navController.navigate("detalle/$postId")
                            },
                            onNavigateToCrear = {
                                navController.navigate("crear")
                            }
                        )
                    }
                    composable(
                        route = "detalle/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getInt("postId") ?: 0
                        PantallaDetalle(
                            viewModel = viewModel,
                            postId = postId,
                            onBack = { navController.popBackStack() },
                            onEdit = { id -> navController.navigate("editar/$id") }
                        )
                    }
                    composable("crear") {
                        PantallaCrear(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "editar/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getInt("postId") ?: 0
                        PantallaEditar(
                            viewModel = viewModel,
                            postId = postId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
