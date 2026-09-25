package com.example.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun ScheduleAppointmentScreen(
    doctorId: Int,
    onBack: () -> Unit,
    onConfirmClick: (String, String, String) -> Unit
) {
    val doctor = MockData.doctors.find { it.id == doctorId } ?: MockData.doctors.first()

    // 📌 SUSTENTACIÓN: Selección única implementada con variables de estado reactivo
    var selectedDate by remember { mutableStateOf(MockData.availableDates[1]) } // "Vie 27"
    var selectedTime by remember { mutableStateOf(MockData.availableTimes[1]) } // "10:30"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Atrás") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Selecciona fecha", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MockData.availableDates.forEach { date ->
                    SelectableChip(
                        text = date,
                        isSelected = date == selectedDate,
                        onClick = { selectedDate = date }, // 📌 SUSTENTACIÓN: Modifica la fecha seleccionada
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selecciona hora", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MockData.availableTimes.forEach { time ->
                    SelectableChip(
                        text = time,
                        isSelected = time == selectedTime,
                        onClick = { selectedTime = time }, // 📌 SUSTENTACIÓN: Modifica la hora seleccionada
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onConfirmClick(doctor.name, selectedDate, selectedTime) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
            ) {
                Text("Confirmar cita", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun SelectableChip(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PurplePrimary else PurpleChipUnselected)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextDark,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}