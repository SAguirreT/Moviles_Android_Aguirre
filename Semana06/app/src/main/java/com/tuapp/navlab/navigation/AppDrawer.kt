package com.tuapp.navlab.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    currentRoute: String,
    onNavegar: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFFE1BEE7)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("MR", fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Maria Rojas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("maria@tecsup.edu.pe", color = Color.Gray, fontSize = 12.sp)
                }
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = currentRoute == Screen.Home.route,
                onClick = { onNavegar(Screen.Home.route); onCloseDrawer() },
                icon = { Icon(Icons.Default.Home, contentDescription = null) }
            )

            NavigationDrawerItem(
                label = { Text("Mis pedidos", fontWeight = FontWeight.Bold) },
                selected = true,
                onClick = { onNavegar(Screen.List.route); onCloseDrawer() },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFF3E5F5))
            )

            NavigationDrawerItem(
                label = { Text("Favoritos") },
                selected = false,
                onClick = { onCloseDrawer() },
                icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = null) }
            )

            NavigationDrawerItem(
                label = { Text("Perfil") },
                selected = currentRoute == Screen.Profile.route,
                onClick = { onNavegar(Screen.Profile.route); onCloseDrawer() },
                icon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            NavigationDrawerItem(
                label = { Text("Cerrar sesion") },
                selected = false,
                onClick = { onNavegar(Screen.Home.route); onCloseDrawer() },
                icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
            )
        }
    }
}