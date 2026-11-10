package com.tecsup.laptops.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import com.tecsup.laptops.ui.components.Cargando
import com.tecsup.laptops.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("< Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Acción para agregar laptop */ }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            when (val actualState = state) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(
                    mensaje = actualState.mensaje,
                    onReintentar = { viewModel.cargarInventarioAdmin() }
                )
                is UiState.Exito -> AdminInventoryList(items = actualState.datos)
            }
        }
    }
}

@Composable
fun AdminInventoryList(
    items: List<Item>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = item.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Marca: ${item.marca}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$ ${item.precio}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(onClick = { /* Editar */ }) {
                        Text("Editar")
                    }
                }
            }
        }
    }
}