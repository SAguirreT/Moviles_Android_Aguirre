
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
 * COMMIT 5:
 * Mostrar los cinco lugares del campus mediante
 * marcadores azules en Google Maps.
 *
 * Se conserva la funcionalidad del Commit 4:
 * - Camara inicial centrada en Lima.
 * - Animacion hacia la ubicacion del usuario.
 * - Marcador rojo con el titulo "Estás aquí".
 *
 * Este componente solo dibuja la informacion recibida.
 * No consulta repositorios ni calcula distancias.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {

    // Posicion inicial del mapa: Lima, Peru.
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    // COMMIT 4:
    // Animar la camara cuando se obtiene la ubicacion.
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

    // Dibujar el mapa de Google.
    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {

        // COMMIT 4:
        // Mostrar el marcador rojo del usuario.
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

        // COMMIT 5:
        // Recorrer los lugares recibidos desde la pantalla.
        lugares.forEach { elemento ->

            // Obtener los datos del lugar.
            val lugar = elemento.lugar

            // Identificar cada marcador mediante su ID.
            key(lugar.id) {

                // Crear las coordenadas del lugar.
                val posicionLugar = LatLng(
                    lugar.latitud,
                    lugar.longitud
                )

                // Dibujar el marcador azul.
                Marker(
                    state = rememberMarkerState(
                        key = "lugar_${lugar.id}",
                        position = posicionLugar
                    ),

                    // Nombre que aparece al tocar el marcador.
                    title = lugar.nombre,

                    // Categoria del lugar.
                    snippet = lugar.categoria,

                    // Color azul solicitado en el laboratorio.
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}
