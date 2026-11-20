
package com.aguirre.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import com.aguirre.campusgps.R
import com.aguirre.campusgps.model.Lugar
import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.UbicacionUi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * MEJORA IA 3:
 * Mapa nocturno con circulo de cercania.
 *
 * Colores:
 * Rojo: usuario.
 * Verde: lugar mas cercano.
 * Azul: otros lugares.
 * Violeta: radio de cercania.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radioMetros: Int,
    modifier: Modifier = Modifier
) {

    val contexto = LocalContext.current

    // Cargar el estilo nocturno existente.
    val estiloOscuro = remember(contexto) {
        MapStyleOptions.loadRawResourceStyle(
            contexto,
            R.raw.map_style_dark
        )
    }

    val propiedadesMapa = remember(estiloOscuro) {
        MapProperties(
            mapStyleOptions = estiloOscuro
        )
    }

    // Camara inicial en Lima.
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    // Obtener la posicion actual cuando este disponible.
    val posicionUsuario = (estado as? UbicacionUi.Lista)
        ?.posicion
        ?.let { posicion ->
            LatLng(
                posicion.latitud,
                posicion.longitud
            )
        }

    // Mover la camara a la ubicacion obtenida.
    LaunchedEffect(posicionUsuario) {

        if (posicionUsuario != null) {

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    posicionUsuario,
                    16f
                ),
                durationMs = 1000
            )
        }
    }

    // Mover la camara al lugar seleccionado.
    LaunchedEffect(seleccionado) {

        if (seleccionado != null) {

            val lugar = lugares.firstOrNull {
                it.lugar.id == seleccionado
            }?.lugar

            if (lugar != null) {

                camara.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lugar.latitud,
                            lugar.longitud
                        ),
                        17f
                    ),
                    durationMs = 1000
                )
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = propiedadesMapa
    ) {

        // MEJORA IA 3:
        // Dibujar circulo alrededor del usuario.
        if (posicionUsuario != null) {

            Circle(
                center = posicionUsuario,
                radius = radioMetros.toDouble(),
                strokeColor = Color(0xFFA78BFA),
                strokeWidth = 4f,
                fillColor = Color(0x338B5CF6),
                clickable = false
            )

            // Marcador rojo del usuario.
            Marker(
                state = rememberMarkerState(
                    key = "ubicacion_actual",
                    position = posicionUsuario
                ),
                title = "Estás aquí"
            )
        }

        // Dibujar los lugares filtrados.
        lugares.forEach { elemento ->

            val lugar = elemento.lugar

            key(lugar.id) {

                val posicionLugar = LatLng(
                    lugar.latitud,
                    lugar.longitud
                )

                // El mas cercano sigue siendo verde.
                val colorMarcador =
                    if (masCercano?.id == lugar.id) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_AZURE
                    }

                val estadoRadio = when {

                    elemento.metros == null ->
                        "Distancia no disponible"

                    elemento.metros <= radioMetros ->
                        "Dentro del radio de $radioMetros m"

                    else ->
                        "Fuera del radio de $radioMetros m"
                }

                Marker(
                    state = rememberMarkerState(
                        key = "lugar_${lugar.id}",
                        position = posicionLugar
                    ),
                    title = lugar.nombre,
                    snippet = "${lugar.categoria} · $estadoRadio",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        colorMarcador
                    )
                )
            }
        }
    }
}
