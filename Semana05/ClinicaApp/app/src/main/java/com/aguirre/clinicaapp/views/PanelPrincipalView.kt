package com.aguirre.clinicaapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aguirre.clinicaapp.routes.Destinos

@Composable
fun PanelPrincipalView(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(ColorPrimarioOscuro, ColorPrincipalPúrpura))).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Panel de Control", color = Color.White, fontSize = 16.sp)
        Text("Dra. Sheila Aguirre", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { navController.navigate(Destinos.VistaDirectorio.ruta) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) { Text("Directorio de Médicos", color = ColorPrimarioOscuro, fontWeight = FontWeight.Bold) }
        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = { navController.navigate(Destinos.VistaPerfilUsuario.ruta) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorDetalleClaro)
        ) { Text("Mi Cuenta", color = ColorPrimarioOscuro, fontWeight = FontWeight.Bold) }
    }
}