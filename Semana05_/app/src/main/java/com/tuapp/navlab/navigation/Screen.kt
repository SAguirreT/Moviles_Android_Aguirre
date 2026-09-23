package com.tuapp.navlab.navigation

sealed class Screen(val route: String) {
    // pantalla incial
    object Home : Screen(route = "home")

    // lista d elementos
    object List : Screen(route = "list")

    // usuario
    object Profile : Screen(route = "profile")
    object Detail : Screen(route = "detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}