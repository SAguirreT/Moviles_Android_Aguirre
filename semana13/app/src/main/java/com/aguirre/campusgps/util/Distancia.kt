
package com.aguirre.campusgps.util

import android.location.Location
import com.aguirre.campusgps.model.Lugar
import com.aguirre.campusgps.model.Posicion

/**
 * COMMIT 6:
 * Calcular la distancia en metros entre la ubicacion
 * actual del usuario y un lugar del campus.
 *
 * Utiliza Location.distanceBetween de Android.
 */
fun distanciaMetros(
    desde: Posicion,
    hasta: Lugar
): Float {

    // Arreglo donde Android almacenara la distancia.
    val resultado = FloatArray(1)

    // Calcular la distancia entre ambas coordenadas.
    Location.distanceBetween(
        desde.latitud,
        desde.longitud,
        hasta.latitud,
        hasta.longitud,
        resultado
    )

    // Devolver la distancia calculada en metros.
    return resultado[0]
}
