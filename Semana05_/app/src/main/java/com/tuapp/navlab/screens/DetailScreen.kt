package com.tuapp.navlab.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    // Estructura Scaffold para mantener consistencia con la TopAppBar superior
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Elemento") },
                navigationIcon = {
                    // Retorno a la pantalla anterior
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(
                text = "ID recibido: $itemId",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Se ha recibido el parámetro de navegación correctamente desde la lista.")
        }
    }
}