
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * COMMIT 3:
 * Pantalla principal de CampusGPS.
 *
 * Solicita los permisos de ubicacion en tiempo de ejecucion
 * y muestra los estados del ViewModel.
 */
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {
    // Observar el estado del ViewModel.
    val estado by vm.estado.collectAsState()

    // Contexto utilizado exclusivamente en la capa UI.
    val contexto = LocalContext.current

    // Solicitar ambos permisos mediante Android.
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        // Basta con que uno de los permisos sea concedido.
        val permisoConcedido = resultado.values.any { it }

        if (permisoConcedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    // Comprobar si ya existe permiso de ubicacion precisa.
    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Comprobar si ya existe permiso de ubicacion aproximada.
    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Estado para deshabilitar el boton durante la busqueda.
    val buscando = estado is UbicacionUi.Buscando

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "CampusGPS",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Encuentra tu ubicacion actual",
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        // Boton principal del laboratorio.
        Button(
            enabled = !buscando,
            onClick = {
                if (permisoFino || permisoAproximado) {

                    // Ya existe permiso: buscar directamente.
                    vm.buscar()

                } else {

                    // No existe permiso: solicitarlo a Android.
                    lanzador.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        ) {
            Text("Obtener mi ubicación")
        }

        // Dibujar el contenido segun el estado recibido.
        when (val actual = estado) {

            is UbicacionUi.Inicial -> {
                Text(
                    text = "Presiona el botón para obtener tu ubicación.",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            is UbicacionUi.Buscando -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = "Buscando ubicación...",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            is UbicacionUi.Lista -> {
                Text(
                    text = "Latitud: ${actual.posicion.latitud}",
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = "Longitud: ${actual.posicion.longitud}"
                )
            }

            is UbicacionUi.Error -> {
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
