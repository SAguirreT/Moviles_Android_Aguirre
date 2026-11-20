
package com.aguirre.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.UbicacionUi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * COMMIT 4:
 * Implementar Google Maps para visualizar la ubicacion
 * actual del usuario mediante un marcador.
 *
 * Funcionalidades:
 * - Mostrar inicialmente el mapa de Lima.
 * - Configurar un zoom inicial de 12.
 * - Animar la camara hacia la ubicacion actual.
 * - Aplicar un zoom de 16 al obtener la ubicacion.
 * - Mostrar un marcador rojo con el titulo "Estás aquí".
 *
 * Los marcadores del campus se agregaran en el Commit 5.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {

    // Configurar la posicion inicial del mapa en Lima.
    val camara = rememberCameraPositionState {

        position = CameraPosition.fromLatLngZoom(
            LatLng(
                -12.0464,
                -77.0428
            ),
            12f
        )
    }

    // Detectar cuando el ViewModel obtiene la ubicacion.
    LaunchedEffect(estado) {

        if (estado is UbicacionUi.Lista) {

            // Obtener las coordenadas actuales del usuario.
            val posicionActual = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            // Animar la camara hasta la ubicacion obtenida.
            camara.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    posicionActual,
                    16f
                ),
                durationMs = 1000
            )
        }
    }

    // Dibujar el mapa utilizando Google Maps Compose.
    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {

        // Mostrar el marcador solo cuando exista ubicacion.
        if (estado is UbicacionUi.Lista) {

            // Crear las coordenadas del marcador.
            val posicionActual = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            // Crear el marcador rojo de la ubicacion actual.
            Marker(
                state = rememberMarkerState(
                    key = "ubicacion_actual",
                    position = posicionActual
                ),
                title = "Estás aquí"
            )
        }

        // COMMIT 5:
        // Aqui se agregaran los cinco marcadores azules
        // correspondientes a los lugares del campus.
    }
}
