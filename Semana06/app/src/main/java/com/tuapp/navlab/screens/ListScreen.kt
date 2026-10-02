package com.tuapp.navlab.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StoreProduct(val id: Int, val name: String, val price: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(onOpenDrawer: () -> Unit) {
    val products = listOf(
        StoreProduct(1, "Audifonos", "S/ 89.00"),
        StoreProduct(2, "Smartwatch", "S/ 199.00"),
        StoreProduct(3, "Funda celular", "S/ 25.00")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Store", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Mas vendidos", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                StoreProductCard(product = product, isFirst = product.id == 1)
            }
        }
    }
}

@Composable
fun StoreProductCard(product: StoreProduct, isFirst: Boolean) {
    var expanded by remember { mutableStateOf(isFirst) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = Color(0xFFE1BEE7)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFF4A148C)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(product.price, color = Color.Gray, fontSize = 14.sp)
                    }
                }
                Box {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Favoritos") },
                            leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                            onClick = { expanded = false }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Compartir") },
                            leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                            onClick = { expanded = false }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Reportar") },
                            leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null) },
                            onClick = { expanded = false }
                        )
                    }
                }
            }
        }
    }
}