
package com.aguirre.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier

import com.aguirre.campusgps.model.Lugar
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
 * COMMIT 8:
 * Mostrar la ubicacion actual y los lugares del campus.
 *
 * Colores:
 * - Rojo: ubicacion actual del usuario.
 * - Verde: lugar mas cercano.
 * - Azul: los demas lugares.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    modifier: Modifier = Modifier
) {

    // Posicion inicial: Lima, Peru.
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    /**
     * COMMIT 4:
     * Animar la camara hacia la ubicacion del usuario.
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
     * Mover la camara al lugar seleccionado.
     */
    LaunchedEffect(seleccionado) {

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

    // Dibujar Google Maps.
    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
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

        // Marcadores de los cinco lugares del campus.
        lugares.forEach { elemento ->

            val lugar = elemento.lugar

            key(lugar.id) {

                val posicionLugar = LatLng(
                    lugar.latitud,
                    lugar.longitud
                )

                // COMMIT 8:
                // Determinar si este es el lugar mas cercano.
                val esMasCercano = masCercano?.id == lugar.id

                // Verde para el mas cercano; azul para los demas.
                val colorMarcador = if (esMasCercano) {
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
