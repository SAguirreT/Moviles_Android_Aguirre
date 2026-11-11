package com.tecsup.laptops.ui.lista

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import com.tecsup.laptops.ui.components.Cargando
import com.tecsup.laptops.ui.components.EmptyState
import com.tecsup.laptops.ui.components.ErrorView
import com.tecsup.laptops.ui.components.ItemCard
import com.tecsup.laptops.ui.theme.Dimens
import com.tecsup.laptops.ui.theme.LaptopsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    esAdmin: Boolean = false,
    onLaptopClick: (Int) -> Unit,
    onIrAFavoritos: () -> Unit,
    onIrAAdmin: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val aviso by viewModel.aviso.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val notifConcedida = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    LaunchedEffect(aviso) {
        aviso?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            viewModel.limpiarAviso()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Catálogo de Laptops",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.refrescar() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sincronizar",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = onIrAFavoritos) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Mis Favoritos",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    if (esAdmin) {
                        IconButton(onClick = onIrAAdmin) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Panel Admin",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (!notifConcedida) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notificaciones desactivadas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = Dimens.M, vertical = Dimens.XS)
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val actualState = state) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(
                        mensaje = actualState.mensaje,
                        onReintentar = { viewModel.cargarLaptops() }
                    )
                    is UiState.Exito -> {
                        if (actualState.datos.isEmpty()) {
                            EmptyState(
                                titulo = "Catálogo vacío",
                                mensaje = "No hay elementos guardados"
                            )
                        } else {
                            ListaContent(
                                items = actualState.datos,
                                onItemClick = onLaptopClick,
                                onToggleFavorito = { viewModel.toggleFavorito(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListaContent(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    onToggleFavorito: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.M),
        verticalArrangement = Arrangement.spacedBy(Dimens.M)
    ) {
        items(items) { laptop ->
            ItemCard(
                item = laptop,
                onClick = { onItemClick(laptop.id) },
                onToggleFavorito = { onToggleFavorito(laptop) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaScreenPreview() {
    LaptopsTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ListaContent(
                items = listOf(
                    Item(1, "MacBook Pro 16 M3 Max", "Laptop de alto rendimiento", 2499.99, "Apple", null, false),
                    Item(2, "Dell XPS 15 OLED", "Pantalla 4K OLED e Intel i9", 1999.99, "Dell", null, true)
                ),
                onItemClick = {},
                onToggleFavorito = {}
            )
        }
    }
}
