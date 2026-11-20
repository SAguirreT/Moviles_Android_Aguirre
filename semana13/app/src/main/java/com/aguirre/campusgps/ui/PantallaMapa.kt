
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
 * MEJORA IA 3:
 * Pantalla con:
 * - Modo oscuro.
 * - Filtros por categoria.
 * - Selector de radio.
 * - Circulo en Google Maps.
 * - Lista con indicadores de cercania.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {

    // Estados originales.
    val estado by vm.estado.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    // MEJORA 2: filtros.
    val categoriaSeleccionada by
    vm.categoriaSeleccionada.collectAsState()

    val lugaresFiltrados by
    vm.lugaresFiltrados.collectAsState()

    // MEJORA 3: radio y contador.
    val radioMetros by vm.radioMetros.collectAsState()

    val cantidadDentroRadio by
    vm.cantidadDentroRadio.collectAsState()

    val contexto = LocalContext.current

    // Solicitar permisos.
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->

        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    // Comprobar permiso preciso.
    val permisoFino = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Comprobar permiso aproximado.
    val permisoAproximado = ContextCompat.checkSelfPermission(
        contexto,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val buscando = estado is UbicacionUi.Buscando

    // Distancia del mas cercano.
    val distanciaMasCercano = lugaresFiltrados
        .firstOrNull {
            it.lugar.id == masCercano?.id
        }
        ?.metros

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoOscuro,

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

            // Mapa con circulo de radio.
            MapaUbicacion(
                estado = estado,
                lugares = lugaresFiltrados,
                seleccionado = seleccionado,
                masCercano = masCercano,
                radioMetros = radioMetros,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Panel inferior.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoOscuro)
                    .padding(vertical = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Botones principales.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

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

                // Informacion del mas cercano.
                if (masCercano != null) {

                    val nombre = masCercano?.nombre.orEmpty()

                    val distancia = distanciaMasCercano?.let {
                        formatearDistancia(it)
                    } ?: "--"

                    Text(
                        text = "📍 $nombre · $distancia",
                        color = VioletaClaro,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 3.dp
                        )
                    )
                }

                // Indicador de carga.
                if (buscando) {

                    CircularProgressIndicator(
                        modifier = Modifier.padding(6.dp),
                        color = VioletaClaro
                    )
                }

                // Errores del Commit 9.
                val errorActual = estado as? UbicacionUi.Error

                if (errorActual != null) {

                    MensajeError(
                        error = errorActual,
                        onReintentar = {
                            vm.buscar()
                        }
                    )
                }

                // MEJORA 2: filtros por categoria.
                Text(
                    text = "FILTRAR POR CATEGORÍA",
                    color = TextoSecundario,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 14.dp,
                            top = 5.dp
                        )
                )

                FiltrosCategoria(
                    categoriaSeleccionada = categoriaSeleccionada,
                    onCategoriaSeleccionada = { categoria ->
                        vm.cambiarCategoria(categoria)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // MEJORA 3: selector de radio.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 3.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "RADIO DE CERCANÍA",
                        color = TextoSecundario,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$cantidadDentroRadio dentro",
                        color = VioletaClaro,
                        fontSize = 11.sp
                    )
                }

                SelectorRadio(
                    radioSeleccionado = radioMetros,
                    onRadioSeleccionado = { radio ->
                        vm.cambiarRadio(radio)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                // Encabezado de la lista.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 4.dp
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
                        text = "${lugaresFiltrados.size} lugares",
                        color = VioletaClaro,
                        fontSize = 12.sp
                    )
                }
            }

            // Lista con indicadores dentro/fuera.
            ListaLugares(
                lugares = lugaresFiltrados,
                seleccionado = seleccionado,
                radioMetros = radioMetros,
                onClick = { id ->
                    vm.seleccionar(id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
            )
        }
    }
}
