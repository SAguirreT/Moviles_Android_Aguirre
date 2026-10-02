package com.tuapp.navlab.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DrawerItem(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun AppDrawer(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    val items = listOf(
        DrawerItem(Screen.Home.route, "Inicio", Icons.Default.Home),
        DrawerItem(Screen.List.route, "Lista / Pedidos", Icons.Default.List),
        DrawerItem(Screen.Profile.route, "Perfil", Icons.Default.Person)
    )

    ModalDrawerSheet {
        // ENCABEZADO DE USUARIO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MR",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Maria Rojas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "maria@tecsup.edu.pe",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ITEMS DE NAVEGACIÓN
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                selected = rutaActual == item.ruta,
                onClick = { onNavegar(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.titulo) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider()

        // BOTÓN CERRAR SESIÓN
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = { onNavegar(Screen.Login.route) },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}