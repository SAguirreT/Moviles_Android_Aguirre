
package com.aguirre.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.ui.theme.BordeOscuro
import com.aguirre.campusgps.ui.theme.FondoOscuro
import com.aguirre.campusgps.ui.theme.SuperficieOscura
import com.aguirre.campusgps.ui.theme.TextoPrincipal
import com.aguirre.campusgps.ui.theme.TextoSecundario
import com.aguirre.campusgps.ui.theme.VioletaClaro
import com.aguirre.campusgps.ui.theme.VioletaSeleccion
import com.aguirre.campusgps.util.formatearDistancia

/**
 * MEJORA IA 3:
 * Tarjetas oscuras con indicador de radio.
 *
 * Mantiene:
 * - Orden de distancias.
 * - Seleccion de lugares.
 * - Categorias.
 */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    radioMetros: Int,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(FondoOscuro),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {

        items(
            items = lugares,
            key = { it.lugar.id }
        ) { elemento ->

            val lugar = elemento.lugar
            val estaSeleccionado = seleccionado == lugar.id

            val distancia = elemento.metros

            val dentroRadio =
                distancia != null &&
                        distancia <= radioMetros

            val icono = when {

                lugar.nombre.contains(
                    "Biblioteca",
                    ignoreCase = true
                ) -> "📚"

                lugar.nombre.contains(
                    "Cafetería",
                    ignoreCase = true
                ) -> "☕"

                lugar.nombre.contains(
                    "Laboratorio",
                    ignoreCase = true
                ) -> "💻"

                lugar.nombre.contains(
                    "Auditorio",
                    ignoreCase = true
                ) -> "🎭"

                lugar.nombre.contains(
                    "Estacionamiento",
                    ignoreCase = true
                ) -> "🅿️"

                else -> "📍"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clickable {
                        onClick(lugar.id)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (estaSeleccionado) {
                        VioletaSeleccion
                    } else {
                        SuperficieOscura
                    }
                ),
                border = BorderStroke(
                    width = if (estaSeleccionado) 2.dp else 1.dp,
                    color = if (estaSeleccionado) {
                        VioletaClaro
                    } else {
                        BordeOscuro
                    }
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Icono del lugar.
                    Row(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                VioletaSeleccion,
                                RoundedCornerShape(12.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = icono,
                            fontSize = 21.sp
                        )
                    }

                    // Informacion del lugar.
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {

                        Text(
                            text = lugar.nombre,
                            color = TextoPrincipal,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.size(4.dp)
                        )

                        Text(
                            text = lugar.categoria,
                            color = TextoSecundario,
                            fontSize = 11.sp
                        )

                        Spacer(
                            modifier = Modifier.size(5.dp)
                        )

                        // MEJORA IA 3:
                        // Indicador de cercania.
                        val mensaje = when {

                            distancia == null ->
                                "Ubicación pendiente"

                            dentroRadio ->
                                "● Dentro del radio"

                            else ->
                                "○ Fuera del radio"
                        }

                        val colorEstado = when {

                            distancia == null ->
                                TextoSecundario

                            dentroRadio ->
                                Color(0xFF86EFAC)

                            else ->
                                TextoSecundario
                        }

                        Text(
                            text = mensaje,
                            color = colorEstado,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Distancia calculada.
                    Text(
                        text = distancia?.let {
                            formatearDistancia(it)
                        } ?: "--",
                        color = VioletaClaro,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
