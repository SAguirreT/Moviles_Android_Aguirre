
package com.aguirre.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * MEJORA IA 1:
 * Configuracion de Material 3 en modo oscuro.
 */
private val ColoresCampusGPS = darkColorScheme(
    primary = VioletaPrimario,
    onPrimary = TextoPrincipal,
    primaryContainer = VioletaSeleccion,
    onPrimaryContainer = TextoPrincipal,

    secondary = VioletaClaro,
    onSecondary = FondoOscuro,

    background = FondoOscuro,
    onBackground = TextoPrincipal,

    surface = SuperficieOscura,
    onSurface = TextoPrincipal,

    surfaceVariant = SuperficieElevada,
    onSurfaceVariant = TextoSecundario,

    outline = BordeOscuro,

    error = ErrorTexto,
    onError = FondoOscuro,
    errorContainer = ErrorFondo,
    onErrorContainer = ErrorTexto
)

/**
 * Tema principal utilizado por MainActivity.
 *
 * Se fuerza el modo oscuro independientemente
 * de la configuracion del telefono.
 */
@Composable
fun CampusGPSTema(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresCampusGPS,
        content = content
    )
}

/**
 * Alias para mantener compatibilidad con
 * archivos que utilicen el nombre en ingles.
 */
@Composable
fun CampusGPSTheme(
    content: @Composable () -> Unit
) {
    CampusGPSTema(content = content)
}
