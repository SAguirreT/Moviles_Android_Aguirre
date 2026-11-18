package com.aguirre.campusgps.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val colores = lightColorScheme(primary = Color(0xFF14699A), secondary = Color(0xFF14699A))
@Composable fun CampusGPSTema(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colores, content = content)
}
