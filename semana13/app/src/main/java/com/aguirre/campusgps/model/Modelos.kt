
package com.aguirre.campusgps.model

/**
 * Representa las coordenadas del usuario.
 */
data class Posicion(
    val latitud: Double,
    val longitud: Double
)

/**
 * Representa un lugar del campus.
 */
data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double
)

/**
 * Representa un lugar y su distancia calculada.
 */
data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float?
)

/**
 * COMMIT 9:
 * Clasificar los errores de ubicacion.
 */
enum class TipoError {
    SIN_PERMISO,
    GPS_APAGADO,
    SIN_PLAY_SERVICES,
    OTRO
}

/**
 * Estados que puede mostrar la pantalla.
 */
sealed interface UbicacionUi {

    data object Inicial : UbicacionUi

    data object Buscando : UbicacionUi

    data class Lista(
        val posicion: Posicion
    ) : UbicacionUi

    // COMMIT 9: incluir el tipo de error.
    data class Error(
        val mensaje: String,
        val tipo: TipoError = TipoError.OTRO
    ) : UbicacionUi
}
