package com.tuapp.navlab.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tuapp.navlab.screens.DetailScreen
import com.tuapp.navlab.screens.HomeScreen
import com.tuapp.navlab.screens.ListScreen
import com.tuapp.navlab.screens.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onNavegar = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.List.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.List.route) {
                ListScreen(onOpenDrawer = { scope.launch { drawerState.open() } })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(Screen.Detail.route) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
                DetailScreen(navController = navController, itemId = itemId)
            }
        }
    }
}