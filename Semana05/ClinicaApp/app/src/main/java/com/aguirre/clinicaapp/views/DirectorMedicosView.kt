package com.aguirre.clinicaapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ChevronRight
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
import com.aguirre.clinicaapp.data.ProviderClinico
import com.aguirre.clinicaapp.routes.Destinos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectorioMedicosView(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Lista de Especialistas", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorPrincipalPúrpura)) }
    ) { p ->
        LazyColumn(modifier = Modifier.padding(p).fillMaxSize()) {
            items(ProviderClinico.listaMedicos) { med ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { navController.navigate(Destinos.VistaFichaMedico.armarRuta(med.idMedico)) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = med.fotoPerfilUrl, contentDescription = null, modifier = Modifier.size(50.dp).clip(CircleShape))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(med.nombreCompleto, fontWeight = FontWeight.Bold, color = ColorPrimarioOscuro)
                            Text(med.especialidadMedica, color = Color.Gray, fontSize = 13.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.ChevronRight, null, tint = ColorPrincipalPúrpura)
                    }
                }
            }
        }
    }
}