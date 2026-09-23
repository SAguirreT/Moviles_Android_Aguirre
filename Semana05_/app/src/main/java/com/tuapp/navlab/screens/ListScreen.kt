package com.tuapp.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tuapp.navlab.navigation.Screen

val sampleItems = listOf(
    1 to "Juan León - Sistemas",
    2 to "María García - Arquitectura",
    3 to "Carlos Pérez - Medicina",
    4 to "Ana López - Derecho"
)

@Optin(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Directorio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(sampleItems) { item ->
                ListItem(
                    headlineContent = { Text(item.second) },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Detail.createRoute(item.first))
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
