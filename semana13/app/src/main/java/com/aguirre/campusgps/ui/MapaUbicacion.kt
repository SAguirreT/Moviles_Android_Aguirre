
package com.aguirre.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier

import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.UbicacionUi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * COMMIT 7:
 * Mostrar los marcadores y mover la camara
 * cuando el usuario selecciona un lugar.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {

    // Camara inicial centrada en Lima.
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    /**
     * COMMIT 4:
     * Mover la camara hacia la ubicacion actual.
     */
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

    /**
     * COMMIT 7:
     * Detectar la seleccion de una tarjeta.
     *
     * Buscar el lugar por ID y animar la camara.
     */
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

    // Dibujar el mapa.
    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {

        // Marcador rojo del usuario.
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

        // Marcadores azules de los lugares.
        lugares.forEach { elemento ->

            val lugar = elemento.lugar

            key(lugar.id) {

                val posicionLugar = LatLng(
                    lugar.latitud,
                    lugar.longitud
                )

                Marker(
                    state = rememberMarkerState(
                        key = "lugar_${lugar.id}",
                        position = posicionLugar
                    ),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}
