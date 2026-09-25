package com.aguirre.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aguirre.clinicasalud.data.MockData
import com.aguirre.clinicasalud.model.Doctor
import com.example.clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onDoctorClick: (Int) -> Unit
) {
    // 📌 SUSTENTACIÓN: Control del estado de la especialidad seleccionada mediante remember y mutableStateOf
    var selectedSpecialty by remember { mutableStateOf("Cardiología") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Hola, Juan", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurplePrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // 📌 SUSTENTACIÓN: Respetamos los márgenes asignados por Scaffold
                .padding(16.dp)
        ) {
            // 📌 SUSTENTACIÓN: LazyRow para implementar el listado horizontal de filtros
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MockData.specialties) { specialty ->
                    val isSelected = specialty == selectedSpecialty
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSpecialty = specialty },
                        label = { Text(specialty) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = PurpleChipUnselected,
                            selectedContainerColor = PurpleChipSelected,
                            labelColor = TextDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Médicos disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)

            Spacer(modifier = Modifier.height(12.dp))

            // 📌 SUSTENTACIÓN: LazyColumn para optimizar la carga vertical de médicos
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(MockData.doctors) { doctor ->
                    DoctorCard(doctor = doctor, onClick = { onDoctorClick(doctor.id) })
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(PurpleLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = doctor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                Text(text = doctor.specialty, fontSize = 14.sp, color = TextGray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = doctor.rating.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}