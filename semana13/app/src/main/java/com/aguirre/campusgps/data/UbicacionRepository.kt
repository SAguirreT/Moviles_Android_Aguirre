
package com.aguirre.campusgps.data

import com.aguirre.campusgps.model.Posicion

/**
 * Contrato del repositorio de ubicacion.
 *
 * COMMIT 9:
 * Agregar la comprobacion de Google Play Services.
 */
interface UbicacionRepository {

    // Obtener la ubicacion actual.
    suspend fun obtenerPosicion(): Result<Posicion>

    // Comprobar disponibilidad de Google Play Services.
    fun playServicesDisponible(): Boolean
}
