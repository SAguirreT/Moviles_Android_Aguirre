
package com.tecsup.miscursosaguirre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.miscursosaguirre.ui.theme.MisCursosAguirreTheme

// Actividad principal que inicia nuestra aplicación.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga el tema visual y el sistema de autenticación.
        setContent {
            MisCursosAguirreTheme {
                AuthApp()
            }
        }
    }
}
