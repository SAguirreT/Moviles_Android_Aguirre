package com.tuapp.navlab.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de colores Portal Académico Premium (Violeta / Lila neón)
val PurpleDark = Color(0xFF1D0036)      // Morado profundo noche
val PurpleMid = Color(0xFF7B2CBF)       // Violeta eléctrico moderno
val PurpleLight = Color(0xFFE0AAFF)     // Lila neón claro

// Degradado vertical institucional para headers y fondos destacados
val BrandGradient = Brush.verticalGradient(
    colors = listOf(PurpleDark, PurpleMid, PurpleLight)
)

val CardGray = Color(0xFFF1ECFA)        // Gris lila suave
val DetailCardGray = Color(0xFFEBE4F7)  // Contenedor violeta pastel
val BackgroundLilac = Color(0xFFF8F5FE) // Fondo pastel ultra limpio
val ErrorRed = Color(0xFFE53935)        // Rojo de alerta / cierre de sesión

// URL constante para el avatar del usuario principal (Sheila Aguirre)
const val SHEILA_AVATAR_URL = "https://randomuser.me/api/portraits/women/44.jpg"
