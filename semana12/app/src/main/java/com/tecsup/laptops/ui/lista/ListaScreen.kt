package com.tecsup.laptops.ui.lista

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import com.tecsup.laptops.ui.components.Cargando
import com.tecsup.laptops.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    esAdmin: Boolean = false,
    onLaptopClick: (Int) -> Unit,
    onIrAFavoritos: () -> Unit,
    onIrAAdmin: () -> Unit,
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
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Laptops", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.refrescar() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sincronizar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onIrAFavoritos) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Mis Favoritos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (esAdmin) {
                        IconButton(onClick = onIrAAdmin) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Panel Admin"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues).fillMaxSize()) {
            if (!notifConcedida) {
                Text(
                    text = "Notificaciones desactivadas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val actualState = state) {
                    is UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorView(
                        mensaje = actualState.mensaje,
                        onReintentar = { viewModel.cargarLaptops() }
                    )
                    is UiState.Exito -> ListaContent(
                        items = actualState.datos,
                        onItemClick = onLaptopClick,
                        onToggleFavorito = { viewModel.toggleFavorito(it) }
                    )
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context)
        .data(item.imagenUrl)
        .crossfade(true)
        .build()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(72.dp)
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                    error = painterResource(id = android.R.drawable.ic_dialog_alert),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                SuggestionChip(
                    onClick = { },
                    label = { Text(item.marca, fontSize = 12.sp) },
                    modifier = Modifier.height(26.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$ ${item.precio}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            IconButton(
                onClick = { onToggleFavorito() }
            ) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (item.esFavorito) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
