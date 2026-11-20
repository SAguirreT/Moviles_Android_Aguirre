
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
 * COMMIT 9:
 * Pantalla principal con manejo visual de errores.
 *
 * Se conserva:
 * - Barra superior azul.
 * - Mapa con marcadores.
 * - Boton para obtener ubicacion.
 * - Boton para ir al lugar mas cercano.
 * - Lista inferior con distancias.
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
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

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

    // Buscar la distancia del lugar mas cercano.
    val distanciaMasCercano = lugares
        .firstOrNull {
            it.lugar.id == masCercano?.id
        }
        ?.metros

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        // Barra superior azul del laboratorio.
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

            // Mapa superior.
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Zona inferior de controles y lista.
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

                // Boton para navegar al lugar mas cercano.
                OutlinedButton(
                    enabled = masCercano != null,
                    onClick = {

                        masCercano?.let { lugar ->
                            vm.seleccionar(lugar.id)
                        }
                    }
                ) {
                    Text("Ir al más cercano")
                }

                // Mostrar nombre y distancia del lugar mas cercano.
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

                // Mostrar indicador de carga.
                if (buscando) {

                    CircularProgressIndicator(
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // COMMIT 9:
                // Mostrar el recuadro de error correspondiente.
                val errorActual = estado as? UbicacionUi.Error

                if (errorActual != null) {

                    MensajeError(
                        error = errorActual,
                        onReintentar = {

                            // Reintentar la busqueda de ubicacion.
                            vm.buscar()
                        }
                    )
                }

                // Lista inferior de lugares.
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
