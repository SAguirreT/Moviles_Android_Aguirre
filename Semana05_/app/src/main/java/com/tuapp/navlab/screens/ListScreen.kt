package com.tuapp.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tuapp.navlab.navigation.Screen

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    // Generación de lista de 8 elementos
    val items = (1..8).map { "Elemento numero $it" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { padding ->
        // Renderizado optimizado mediante LazyColumn
        LazyColumn(contentPadding = padding) {
            items(count = items.size) { index ->
                ListItem(
                    headlineContent = { Text(items[index]) },
                    supportingContent = { Text("Toca para ver el detalle") },
                    modifier = Modifier.clickable {
                        // Navegación enviando el ID correlativo
                        navController.navigate(Screen.Detail.createRoute(itemId = index + 1))
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
