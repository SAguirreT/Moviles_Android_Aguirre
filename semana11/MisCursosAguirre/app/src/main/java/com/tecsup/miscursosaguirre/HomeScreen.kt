
package com.tecsup.miscursosaguirre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

// Colores compartidos con las pantallas de login y registro.
private val FondoBienvenida = Color(0xFFF8F4FF)
private val MoradoBienvenida = Color(0xFF7956B8)

// Pantalla que se muestra cuando el usuario inicia sesión.
@Composable
fun HomeScreen(
    onCerrarSesion: () -> Unit
) {
    // Obtiene el correo electrónico del usuario autenticado.
    val correoUsuario = FirebaseAuth.getInstance()
        .currentUser?.email ?: "Usuario"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoBienvenida)
    ) {
        // Encabezado pequeño con mi nombre
        Text(
            text = "Sheila Aguirre",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF79668D),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )

        // Contenido principal centrado como en el laboratorio.
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mensaje principal de bienvenida.
            Text(
                text = "Bienvenido/a",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF292333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Muestra el correo de la cuenta autenticada.
            Text(
                text = "Sesión iniciada como: $correoUsuario",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Solicita cerrar sesión mediante la navegación de AuthApp.
            Button(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoBienvenida
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = Color.White
                )
            }
        }
    }
}

