
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
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.aguirre.campusgps.model.TipoError
import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.ui.theme.ErrorFondo
import com.aguirre.campusgps.ui.theme.ErrorTexto

/**
 * MEJORA IA 1:
 * Mostrar errores con colores oscuros.
 *
 * Mantiene las acciones del Commit 9.
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
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .background(
                ErrorFondo,
                RoundedCornerShape(14.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Mensaje del error.
        Text(
            text = error.mensaje,
            color = ErrorTexto,
            fontWeight = FontWeight.Medium
        )

        when (error.tipo) {

            TipoError.SIN_PERMISO -> {

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {

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
                    }
                ) {
                    Text("Abrir ajustes")
                }
            }

            TipoError.GPS_APAGADO -> {

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = onReintentar
                ) {
                    Text("Reintentar")
                }
            }

            TipoError.SIN_PLAY_SERVICES -> {
                // Mostrar mensaje sin boton.
            }

            TipoError.OTRO -> {
                // Mostrar mensaje sin boton.
            }
        }
    }
}
