
package com.aguirre.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext

import androidx.core.content.ContextCompat

import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.ui.theme.FondoOscuro
import com.aguirre.campusgps.ui.theme.TextoPrincipal
import com.aguirre.campusgps.ui.theme.TextoSecundario
import com.aguirre.campusgps.ui.theme.VioletaClaro
import com.aguirre.campusgps.ui.theme.VioletaPrimario
import com.aguirre.campusgps.util.formatearDistancia
import com.aguirre.campusgps.viewmodel.MapaViewModel

/**
 * MEJORA IA 1:
 * Pantalla principal con modo oscuro profesional.
 *
 * Conserva:
 * - Permisos de ubicacion.
 * - Google Maps y marcadores.
 * - Lugar mas cercano.
 * - Lista ordenada.
 * - Manejo de errores.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

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

    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val buscando = estado is UbicacionUi.Buscando

    // Distancia del lugar mas cercano.
    val distanciaMasCercano = lugares
        .firstOrNull {
            it.lugar.id == masCercano?.id
        }
        ?.metros

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoOscuro,

        // Encabezado oscuro.
        topBar = {

            TopAppBar(
                title = {

                    Column {

                        Text(
                            text = "CampusGPS",
                            color = TextoPrincipal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Explora tu campus",
                            color = TextoSecundario,
                            fontSize = 12.sp
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoOscuro
                )
            )
        }

    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .background(FondoOscuro)
        ) {

            // Mapa oscuro.
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Controles inferiores.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoOscuro)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Botones alineados horizontalmente.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Boton principal violeta.
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = !buscando,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VioletaPrimario,
                            contentColor = TextoPrincipal
                        ),
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
                        Text(
                            text = "Mi ubicación",
                            fontSize = 12.sp
                        )
                    }

                    // Boton del lugar mas cercano.
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        enabled = masCercano != null,
                        shape = RoundedCornerShape(12.dp),
                        onClick = {

                            masCercano?.let { lugar ->
                                vm.seleccionar(lugar.id)
                            }
                        }
                    ) {
                        Text(
                            text = "Más cercano",
                            fontSize = 12.sp
                        )
                    }
                }

                // Informacion del lugar mas cercano.
                if (masCercano != null) {

                    val nombre = masCercano?.nombre.orEmpty()

                    val distancia = distanciaMasCercano?.let {
                        formatearDistancia(it)
                    } ?: "--"

                    Text(
                        text = "📍 $nombre · $distancia",
                        color = VioletaClaro,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }

                // Indicador de carga.
                if (buscando) {

                    CircularProgressIndicator(
                        modifier = Modifier.padding(6.dp),
                        color = VioletaClaro
                    )
                }

                // Mensaje de error.
                val errorActual = estado as? UbicacionUi.Error

                if (errorActual != null) {

                    MensajeError(
                        error = errorActual,
                        onReintentar = {
                            vm.buscar()
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // Encabezado de la lista.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 4.dp,
                            vertical = 7.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Lugares cercanos",
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${lugares.size} lugares",
                        color = VioletaClaro,
                        fontSize = 12.sp
                    )
                }
            }

            // Lista inferior con tarjetas oscuras.
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
