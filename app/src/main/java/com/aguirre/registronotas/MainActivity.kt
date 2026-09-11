package com.aguirre.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.aguirre.registronotas.ui.theme.RegistroNotasTheme
val MoradoPrincipal = Color(0xFF6A4C93)
val MoradoClaroFondo = Color(0xFFF3EEFB)
val MoradoBadge = Color(0xFFD8CDEE)
val MoradoClaroBadgeFondo = Color(0xFFE7DFF6)
val GrisTextoSecundario = Color(0xFF6B6B6B)
val VerdeClaroBg = Color(0xFFE3F3E6)
val VerdeTexto = Color(0xFF2E7D32)
val AmbarClaroBg = Color(0xFFFFF1DA)
val AmbarTexto = Color(0xFFB25E00)
val RojoClaroBg = Color(0xFFFCE4E4)
val RojoTexto = Color(0xFFC62828)
val VerdeOscuroBg = Color(0xFFDCEEE0)
val VerdeOscuroTexto = Color(0xFF1B5E20)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroNotasTheme {
                RegistroNotasScreen()
            }
        }
    }
}

annotation class RegistroNotasScreen

data class Curso(val nombre: String, val peso: Float)

val cursos = listOf(
    Curso("Fundamentos de Programación", 0.20f),
    Curso("Programación Orientada a Objetos", 0.25f),
    Curso("Programación en Móviles", 0.30f),
    Curso("Base de Datos", 0.25f)
)