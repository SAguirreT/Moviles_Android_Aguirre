
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat

import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * COMMIT 7:
 * Pantalla principal de CampusGPS.
 *
 * Diseno basado en el laboratorio GLAB-S13:
 * - Barra superior azul.
 * - Boton de ubicacion centrado.
 * - Mapa en la zona superior.
 * - Lista inferior de lugares.
 * - Seleccion de tarjetas y movimiento del mapa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {

    // Observar los estados del ViewModel.
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()

    val contexto = LocalContext.current

    // Color principal similar al documento.
    val azulPrincipal = Color(0xFF176A9C)

    // Solicitar permisos de ubicacion.
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    // Comprobar el permiso preciso.
    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Comprobar el permiso aproximado.
    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val buscando = estado is UbicacionUi.Buscando

    // Estructura principal de Material 3.
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        // Barra superior azul.
        topBar = {

            TopAppBar(
                title = {

                    Text(
                        text = "CampusGPS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = azulPrincipal
                )
            )
        },

        containerColor = Color(0xFFF7F9FC)

    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // COMMIT 7:
            // El mapa ocupa el espacio disponible.
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Zona inferior con el boton y la lista.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9FC))
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Boton principal del laboratorio.
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
                    }
                ) {

                    Text("Obtener mi ubicación")
                }

                // Mostrar el estado de busqueda.
                if (buscando) {

                    CircularProgressIndicator(
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // Mostrar las coordenadas obtenidas.
                if (estado is UbicacionUi.Lista) {

                    val posicion = (estado as UbicacionUi.Lista).posicion

                    Text(
                        text = "Latitud: ${posicion.latitud}  |  Longitud: ${posicion.longitud}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }

                // Mostrar mensajes del estado.
                if (estado is UbicacionUi.Inicial) {

                    Text(
                        text = "Presiona el botón para obtener tu ubicación.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                if (estado is UbicacionUi.Error) {

                    Text(
                        text = (estado as UbicacionUi.Error).mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // COMMIT 7:
                // Lista inferior con altura fija de 220 dp.
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { id ->

                        // Informar al ViewModel que lugar se toco.
                        vm.seleccionar(id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }
    }
}
