
package com.aguirre.campusgps.util

import java.util.Locale
import kotlin.math.roundToInt

/**
 * COMMIT 6:
 * Convertir una distancia en metros a un texto legible.
 *
 * Ejemplos:
 * 88.4 metros -> "88 m"
 * 1500 metros -> "1.5 km"
 */
fun formatearDistancia(metros: Float): String {

    return if (metros < 1000f) {

        // Mostrar metros sin decimales.
        "${metros.roundToInt()} m"

    } else {

        // Convertir metros a kilometros.
        val kilometros = metros / 1000f

        // Mostrar kilometros con un decimal.
        String.format(
            Locale.US,
            "%.1f km",
            kilometros
        )
    }
}
