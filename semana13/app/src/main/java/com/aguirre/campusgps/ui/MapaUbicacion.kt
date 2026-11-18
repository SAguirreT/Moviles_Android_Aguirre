package com.aguirre.campusgps.ui
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.UbicacionUi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

// Esqueleto guiado: se integrará a PantallaMapa en el commit 4.
@Composable
fun MapaUbicacion(estado: UbicacionUi, lugares: List<LugarConDistancia>, seleccionado: Int?, modifier: Modifier = Modifier) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }
    // TODO Paso 3: LaunchedEffect(estado), animación y marcador propio.
    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // TODO Paso 4: marcadores de lugares.
    }
}
