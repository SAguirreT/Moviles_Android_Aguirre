
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding


import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.aguirre.campusgps.util.formatearDistancia
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * COMMIT 8:
 * Pantalla principal con seleccion del lugar mas cercano.
 *
 * Diseno basado en GLAB-S13:
 * - Barra superior azul.
 * - Mapa con marcadores rojos, azules y verdes.
 * - Boton principal para obtener ubicacion.
 * - Boton secundario para ir al lugar mas cercano.
 * - Texto informativo del lugar mas cercano.
 * - Lista inferior de lugares con distancias.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {

    // Observar el estado de la ubicacion.
    val estado by vm.estado.collectAsState()

    // Observar los cinco lugares del campus.
    val lugares by vm.lugares.collectAsState()

    // Observar el lugar seleccionado.
    val seleccionado by vm.seleccionado.collectAsState()

    // COMMIT 8:
    // Observar el lugar mas cercano.
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

    // Colores del laboratorio.
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

    // Buscar la distancia correspondiente al lugar mas cercano.
    val distanciaMasCercano = lugares
        .firstOrNull {
            it.lugar.id == masCercano?.id
        }
        ?.metros

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

            // Mapa superior con marcadores de tres colores.
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Zona inferior del laboratorio.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F9FC))
                    .padding(top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Boton principal.
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

                // COMMIT 8:
                // Boton secundario con borde.
                OutlinedButton(
                    enabled = masCercano != null,
                    onClick = {

                        // Seleccionar el lugar mas cercano.
                        masCercano?.let { lugar ->
                            vm.seleccionar(lugar.id)
                        }
                    }
                ) {
                    Text("Ir al más cercano")
                }

                // Mostrar el nombre y la distancia.
                if (masCercano != null) {

                    Text(
                        text = "Más cercano: ${masCercano!!.nombre} " +
                                "(${distanciaMasCercano?.let {
                                    formatearDistancia(it)
                                } ?: "Sin calcular"})",

                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF253445),
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }

                // Mostrar progreso de busqueda.
                if (buscando) {

                    CircularProgressIndicator(
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // Mostrar mensajes de error.
                if (estado is UbicacionUi.Error) {

                    Text(
                        text = (estado as UbicacionUi.Error).mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Lista inferior de los cinco lugares.
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { id ->
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
