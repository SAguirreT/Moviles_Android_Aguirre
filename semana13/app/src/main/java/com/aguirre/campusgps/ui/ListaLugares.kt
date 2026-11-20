
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.util.formatearDistancia

/**
 * COMMIT 7:
 * Mostrar los lugares del campus en una lista.
 *
 * Cada tarjeta contiene:
 * - Nombre del lugar.
 * - Categoria.
 * - Distancia en metros o kilometros.
 *
 * Al tocar una tarjeta, se comunica el ID seleccionado.
 * Este componente no conoce el ViewModel.
 */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // Colores similares al diseño del laboratorio.
    val azul = Color(0xFF176A9C)
    val azulClaro = Color(0xFFE4F2FB)

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F9FC)),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        items(
            items = lugares,
            key = { it.lugar.id }
        ) { elemento ->

            val lugar = elemento.lugar

            // Comprobar si esta tarjeta fue seleccionada.
            val estaSeleccionado = seleccionado == lugar.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onClick(lugar.id)
                    },

                shape = RoundedCornerShape(9.dp),

                colors = CardDefaults.cardColors(
                    containerColor = if (estaSeleccionado) {
                        azulClaro
                    } else {
                        Color.White
                    }
                ),

                border = BorderStroke(
                    width = if (estaSeleccionado) 2.dp else 1.dp,
                    color = if (estaSeleccionado) {
                        azul
                    } else {
                        Color(0xFFE0E6EC)
                    }
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Punto azul que identifica el lugar.
                    Spacer(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                color = azul,
                                shape = CircleShape
                            )
                    )

                    // Nombre y categoria del lugar.
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {

                        Text(
                            text = lugar.nombre,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF253445)
                        )

                        Text(
                            text = lugar.categoria,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    // Distancia calculada en el Commit 6.
                    Text(
                        text = elemento.metros?.let {
                            formatearDistancia(it)
                        } ?: "Sin calcular",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = azul
                    )
                }
            }
        }
    }
}
