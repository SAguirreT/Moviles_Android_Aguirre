package com.aguirre.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colores = lightColorScheme(
    primary = Color(0xFF12659A),
    onPrimary = Color.White,
    secondary = Color(0xFF12659A),
    background = Color(0xFFF9FAFC),
    surface = Color.White
)

@Composable
fun CentroTareasTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colores, content = content)
}
