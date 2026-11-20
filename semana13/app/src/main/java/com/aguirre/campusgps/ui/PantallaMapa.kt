
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * COMMIT 4:
 * Integra Google Maps con la pantalla principal.
 * Conserva la solicitud de permisos del Commit 3.
 */
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {
    // Observar el estado actual de la ubicacion.
    val estado by vm.estado.collectAsState()

    val contexto = LocalContext.current

    // Solicitar permisos cuando el usuario no los tiene.
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        val concedido = resultado.values.any { it }

        if (concedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    // Comprobar el permiso de ubicacion precisa.
    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Comprobar el permiso de ubicacion aproximada.
    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val buscando = estado is UbicacionUi.Buscando

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Titulo principal.
        Text(
            text = "CampusGPS",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Boton que obtiene la ubicacion actual.
        Button(
            enabled = !buscando,
            onClick = {

                if (permisoFino || permisoAproximado) {
                    vm.buscar()
                } else {
                    lanzador.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text("Obtener mi ubicación")
        }

        // Mensajes correspondientes al estado.
        when (val actual = estado) {

            is UbicacionUi.Inicial -> {
                Text(
                    text = "Presiona el botón para obtener tu ubicación.",
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            is UbicacionUi.Buscando -> {
                CircularProgressIndicator()

                Text(
                    text = "Buscando ubicación...",
                    modifier = Modifier.padding(8.dp)
                )
            }

            is UbicacionUi.Lista -> {
                Text(
                    text = "Latitud: ${actual.posicion.latitud}",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Longitud: ${actual.posicion.longitud}",
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            is UbicacionUi.Error -> {
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // COMMIT 4:
        // El mapa ocupa todo el espacio libre de la pantalla.
        MapaUbicacion(
            estado = estado,
            lugares = emptyList<LugarConDistancia>(),
            seleccionado = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}
