package com.aguirre.campusgps.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("CampusGPS") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary)) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            // TODO Paso 2: comprobar y solicitar permisos antes de llamar a vm.buscar().
            Button(onClick = { vm.buscar() }, enabled = estado !is UbicacionUi.Buscando) { Text("Obtener mi ubicación") }
            Spacer(Modifier.height(12.dp))
            when (val actual = estado) {
                UbicacionUi.Inicial -> Text("Pulsa el botón para conocer tu ubicación")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> {
                    Text("Latitud: ${actual.posicion.latitud}")
                    Text("Longitud: ${actual.posicion.longitud}")
                }
                is UbicacionUi.Error -> Text(actual.mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
