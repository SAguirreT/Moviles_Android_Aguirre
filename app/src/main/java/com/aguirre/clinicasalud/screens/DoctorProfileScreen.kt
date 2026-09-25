package com.example.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinicasalud.data.MockData
import com.example.clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    doctorId: Int, // 📌 SUSTENTACIÓN: Recibe el ID pasado como parámetro por la ruta
    onBack: () -> Unit,
    onScheduleClick: (Int) -> Unit
) {
    val doctor = MockData.doctors.find { it.id == doctorId } ?: MockData.doctors.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(PurpleLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextDark)
            Text("${doctor.specialty} · ${doctor.experience}", fontSize = 14.sp, color = TextGray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${doctor.rating} (${doctor.reviewCount} reseñas)", fontSize = 14.sp, color = TextGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(doctor.bio, fontSize = 14.sp, color = TextGray, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f)) // 📌 SUSTENTACIÓN: Ocupa todo el espacio dinámico disponible

            Button(
                onClick = { onScheduleClick(doctor.id) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
            ) {
                Text("Agendar cita", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}