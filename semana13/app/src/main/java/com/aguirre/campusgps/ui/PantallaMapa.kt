
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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

import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * COMMIT 5:
 * Pantalla principal de CampusGPS.
 *
 * Funcionalidades:
 * - Solicitar permisos de ubicacion.
 * - Obtener latitud y longitud del usuario.
 * - Mostrar Google Maps.
 * - Mostrar el marcador rojo de la ubicacion actual.
 * - Mostrar los cinco marcadores azules del campus.
 *
 * Los lugares se reciben desde el ViewModel para
 * respetar la arquitectura MVVM + Repository.
 */
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {

    // Observar el estado de la ubicacion.
    val estado by vm.estado.collectAsState()

    // Observar los lugares proporcionados por el ViewModel.
    val lugares by vm.lugares.collectAsState()

    // Obtener el contexto de Android.
    val contexto = LocalContext.current

    // Solicitar permisos de ubicacion en tiempo de ejecucion.
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        // Comprobar si se concedio algun permiso.
        val permisoConcedido = resultado.values.any { it }

        if (permisoConcedido) {

            // Obtener ubicacion cuando se concede el permiso.
            vm.buscar()

        } else {

            // Informar al ViewModel que el permiso fue denegado.
            vm.permisoDenegado()
        }
    }

    // Comprobar permiso de ubicacion precisa.
    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Comprobar permiso de ubicacion aproximada.
    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Deshabilitar el boton durante la busqueda.
    val buscando = estado is UbicacionUi.Buscando

    // Contenedor principal de la pantalla.
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Titulo de la aplicacion.
        Text(
            text = "CampusGPS",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Boton para obtener la ubicacion.
        Button(
            enabled = !buscando,
            onClick = {

                if (permisoFino || permisoAproximado) {

                    // Buscar directamente si ya existe permiso.
                    vm.buscar()

                } else {

                    // Solicitar los permisos necesarios.
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

        // Mostrar mensajes segun el estado del ViewModel.
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

                // Mostrar latitud del usuario.
                Text(
                    text = "Latitud: ${actual.posicion.latitud}",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Mostrar longitud del usuario.
                Text(
                    text = "Longitud: ${actual.posicion.longitud}",
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            is UbicacionUi.Error -> {

                // Mostrar el mensaje de error.
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // COMMIT 5:
        // Dibujar Google Maps y enviar los lugares del campus.
        MapaUbicacion(
            estado = estado,

            // Los cinco lugares provienen del ViewModel.
            lugares = lugares,

            // La seleccion se implementara en el Commit 7.
            seleccionado = null,

            // El mapa ocupa todo el espacio disponible.
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}
