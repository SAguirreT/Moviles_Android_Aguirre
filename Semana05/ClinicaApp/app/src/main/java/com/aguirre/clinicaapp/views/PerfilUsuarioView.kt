package com.aguirre.clinicaapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aguirre.clinicaapp.routes.Destinos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioView(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Información de Perfil", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorPrincipalPúrpura)) }
    ) { p ->
        Column(modifier = Modifier.padding(p).fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = "https://randomuser.me/api/portraits/women/44.jpg", contentDescription = null, modifier = Modifier.size(90.dp).clip(CircleShape))
            Spacer(modifier = Modifier.height(14.dp))
            Text("Dra. Sheila Aguirre", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorPrimarioOscuro)
            Text("sheila.aguirre@clinica.pe", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate(Destinos.VistaAcceso.ruta) { popUpTo(0) } },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("Cerrar Sesión") }
        }
    }
}