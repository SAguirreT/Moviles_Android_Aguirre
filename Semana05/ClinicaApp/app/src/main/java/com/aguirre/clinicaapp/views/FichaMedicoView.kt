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
import com.aguirre.clinicaapp.model.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaMedicoView(navController: NavController, medico: Doctor) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(medico.nombreCompleto, color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorPrincipalPúrpura)) }
    ) { p ->
        Column(modifier = Modifier.padding(p).fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = medico.fotoPerfilUrl, contentDescription = null, modifier = Modifier.size(100.dp).clip(CircleShape))
            Spacer(modifier = Modifier.height(14.dp))
            Text(medico.nombreCompleto, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorPrimarioOscuro)
            Text(medico.especialidadMedica, color = ColorPrincipalPúrpura, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Código CMP: ${medico.colegiaturaCmp}", fontWeight = FontWeight.Bold)
                    Text("Contacto: ${medico.correoContacto}")
                    Text("Ubicación: ${medico.ubicacionConsultorio}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(medico.resumenProfesional, color = Color.Gray)
                }
            }
        }
    }
}