
package com.tecsup.miscursosaguirre

import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth

// Controla qué pantalla debe visualizar el usuario.
@Composable
fun AuthApp() {
    val auth = remember { FirebaseAuth.getInstance() }

    // Si existe una sesión activa, se muestra la bienvenida.
    var pantalla by remember {
        mutableStateOf(
            if (auth.currentUser != null) "home" else "login"
        )
    }

    when (pantalla) {
        "login" -> LoginScreen(
            onLoginExitoso = { pantalla = "home" },
            onIrRegistro = { pantalla = "registro" }
        )

        "registro" -> RegisterScreen(
            onRegistroExitoso = { pantalla = "home" },
            onIrLogin = { pantalla = "login" }
        )

        "home" -> HomeScreen(
            onCerrarSesion = {
                // Finaliza la sesión y regresa al login.
                auth.signOut()
                pantalla = "login"
            }
        )
    }
}

