
package com.aguirre.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * MEJORA IA 1:
 * Mapa de Google Maps con estilo nocturno.
 *
 * Marcadores:
 * Rojo: ubicacion actual.
 * Verde: lugar mas cercano.
 * Azul: otros lugares.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    modifier: Modifier = Modifier
) {

    val contexto = LocalContext.current

    // Cargar el estilo oscuro del mapa.
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

    // Posicion inicial del mapa.
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    // Mover la camara a la ubicacion del usuario.
    LaunchedEffect(estado) {

        if (estado is UbicacionUi.Lista) {

            val posicionActual = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    posicionActual,
                    16f
                ),
                durationMs = 1000
            )
        }
    }

    // Mover la camara al lugar seleccionado.
    LaunchedEffect(seleccionado, lugares) {

        if (seleccionado != null) {

            val lugarSeleccionado = lugares
                .firstOrNull {
                    it.lugar.id == seleccionado
                }
                ?.lugar

            if (lugarSeleccionado != null) {

                val destino = LatLng(
                    lugarSeleccionado.latitud,
                    lugarSeleccionado.longitud
                )

                camara.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        destino,
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

        // Marcador rojo de la ubicacion actual.
        if (estado is UbicacionUi.Lista) {

            val posicionActual = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            Marker(
                state = rememberMarkerState(
                    key = "ubicacion_actual",
                    position = posicionActual
                ),
                title = "Estás aquí"
            )
        }

        // Marcadores de los lugares.
        lugares.forEach { elemento ->

            val lugar = elemento.lugar

            key(lugar.id) {

                val posicionLugar = LatLng(
                    lugar.latitud,
                    lugar.longitud
                )

                val colorMarcador =
                    if (masCercano?.id == lugar.id) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_AZURE
                    }

                Marker(
                    state = rememberMarkerState(
                        key = "lugar_${lugar.id}",
                        position = posicionLugar
                    ),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        colorMarcador
                    )
                )
            }
        }
    }
}
