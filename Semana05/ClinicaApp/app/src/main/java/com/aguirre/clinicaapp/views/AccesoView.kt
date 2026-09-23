package com.aguirre.clinicaapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aguirre.clinicaapp.routes.Destinos

// COLOR DECLARATIONS (Disponibles para las demás pantallas)
val ColorPrimarioOscuro = Color(0xFF1B1B2F)
val ColorPrincipalPúrpura = Color(0xFF6B2D5C)
val ColorDetalleClaro = Color(0xFFE2C4D9)
val FondoFondoGrisFrio = Color(0xFFF5F5FA)

@Composable
fun AccesoView(navController: NavController) {
    var usuarioText by remember { mutableStateOf("") }
    var claveText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(FondoFondoGrisFrio), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f).border(1.dp, ColorDetalleClaro, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.HealthAndSafety, null, tint = ColorPrincipalPúrpura, modifier = Modifier.size(52.dp))
                Text("Sistema Clínico", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ColorPrimarioOscuro)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = usuarioText, onValueChange = { usuarioText = it }, label = { Text("Usuario / CMP") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = claveText, onValueChange = { claveText = it }, label = { Text("Clave de Acceso") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = { navController.navigate(Destinos.VistaPanel.ruta) { popUpTo(Destinos.VistaAcceso.ruta) { inclusive = true } } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrincipalPúrpura)
                ) { Text("INGRESAR", color = Color.White) }
            }
        }
    }
}