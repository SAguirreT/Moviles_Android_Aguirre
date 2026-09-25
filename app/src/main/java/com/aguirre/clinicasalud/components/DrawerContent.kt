package com.aguirre.clinicasalud.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aguirre.clinicasalud.ui.theme.*
import com.example.clinicasalud.ui.theme.PurpleLight
import com.example.clinicasalud.ui.theme.PurplePrimary
import com.example.clinicasalud.ui.theme.TextDark

@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    // 📌 SUSTENTACIÓN: ModalDrawerSheet es el contenedor M3 del menú deslizable
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header con iniciales y usuario
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(PurpleLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JP", color = PurplePrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Juan Pérez", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                    Text("Paciente", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            val items = listOf(
                "home" to "Inicio",
                "my_appointments" to "Mis citas",
                "medical_history" to "Historial médico",
                "profile" to "Perfil"
            )

            // 📌 SUSTENTACIÓN: Resalta dinámicamente la opción del menú correspondiente a la pantalla visible
            items.forEach { (route, title) ->
                val isSelected = currentRoute == route
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PurpleLight else Color.Transparent)
                        .clickable { onNavigate(route) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PurplePrimary else Color.Transparent)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(if (isSelected) PurpleLight else Color.Gray)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PurplePrimary else TextDark,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}