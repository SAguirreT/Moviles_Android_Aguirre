package com.tuapp.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tuapp.navlab.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val items = (1..8).map { "Elemento numero $it" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Productos") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("< Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(count = items.size) { index ->
                var expanded by remember { mutableStateOf(false) }

                ListItem(
                    headlineContent = { Text(items[index]) },
                    supportingContent = { Text("Toca para ver el detalle") },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Detail.createRoute(itemId = index + 1))
                    },
                    trailingContent = {
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Opciones del elemento"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Favoritos") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Favorite, contentDescription = "Favoritos")
                                    },
                                    onClick = { expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Compartir") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                                    },
                                    onClick = { expanded = false }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Reportar") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Warning, contentDescription = "Reportar")
                                    },
                                    onClick = { expanded = false }
                                )
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}