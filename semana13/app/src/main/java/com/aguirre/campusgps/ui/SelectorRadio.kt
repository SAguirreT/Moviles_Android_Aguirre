
package com.aguirre.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.aguirre.campusgps.ui.theme.TextoPrincipal
import com.aguirre.campusgps.ui.theme.VioletaPrimario

/**
 * MEJORA IA 3:
 * Selector visual del radio de cercania.
 *
 * La logica pertenece al ViewModel.
 */
@Composable
fun SelectorRadio(
    radioSeleccionado: Int,
    onRadioSeleccionado: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val radios = listOf(100, 200, 500)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        radios.forEach { radio ->

            val activo = radioSeleccionado == radio

            if (activo) {

                Button(
                    onClick = {
                        onRadioSeleccionado(radio)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VioletaPrimario,
                        contentColor = TextoPrincipal
                    )
                ) {
                    Text(
                        text = "$radio m",
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {

                OutlinedButton(
                    onClick = {
                        onRadioSeleccionado(radio)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("$radio m")
                }
            }
        }
    }
}
