
package com.aguirre.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.aguirre.campusgps.model.TipoError
import com.aguirre.campusgps.model.UbicacionUi

/**
 * COMMIT 9:
 * Mostrar mensajes de error y acciones de recuperacion.
 *
 * SIN_PERMISO: Abrir ajustes de la aplicacion.
 * GPS_APAGADO: Reintentar.
 * SIN_PLAY_SERVICES: Mostrar mensaje sin boton.
 * OTRO: Mostrar mensaje sin cerrar la app.
 */
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {

    val contexto = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color = Color(0xFFFFE8E8),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Mostrar el mensaje de error.
        Text(
            text = error.mensaje,
            color = Color(0xFF9D2525),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )

        // Mostrar una accion segun el tipo de error.
        when (error.tipo) {

            TipoError.SIN_PERMISO -> {

                Button(
                    onClick = {

                        // Abrir los ajustes de esta aplicacion.
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        ).apply {

                            data = Uri.fromParts(
                                "package",
                                contexto.packageName,
                                null
                            )
                        }

                        contexto.startActivity(intent)
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Abrir ajustes")
                }
            }

            TipoError.GPS_APAGADO -> {

                Button(
                    onClick = onReintentar,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Reintentar")
                }
            }

            TipoError.SIN_PLAY_SERVICES -> {
                // El laboratorio indica que no debe haber boton.
            }

            TipoError.OTRO -> {
                // Mostrar solamente el mensaje de error.
            }
        }
    }
}
