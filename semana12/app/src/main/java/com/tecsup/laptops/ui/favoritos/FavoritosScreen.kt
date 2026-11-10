package com.tecsup.laptops.ui.favoritos

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
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            when (val actualState = state) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(
                    mensaje = actualState.mensaje,
                    onReintentar = { viewModel.cargarFavoritos() }
                )
                is UiState.Exito -> {
                    if (actualState.datos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text("No tienes laps guardadas en favoritos.")
                        }
                    } else {
                        FavoritosList(items = actualState.datos)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritosList(
    items: List<Item>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { laptop ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = laptop.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Marca: ${laptop.marca}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "$ ${laptop.precio}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}