package com.example.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clinicasalud.ui.theme.*

@Composable
fun ConfirmationScreen(
    doctorName: String,
    date: String,
    time: String,
    onViewAppointmentsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 📌 SUSTENTACIÓN: Círculo de éxito en tono verde suave
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(StatusGreenBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = StatusGreenText, modifier = Modifier.size(50.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("¡Cita agendada!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextDark)
        Spacer(modifier = Modifier.height(8.dp))
        Text(doctorName, fontSize = 16.sp, color = TextGray)
        Text("$date, $time am", fontSize = 14.sp, color = TextGray)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onViewAppointmentsClick,
            modifier = Modifier.fillMaxWidth(0.7f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleChipUnselected)
        ) {
            Text("Ver mis citas", color = TextDark, fontSize = 15.sp)
        }
    }
}