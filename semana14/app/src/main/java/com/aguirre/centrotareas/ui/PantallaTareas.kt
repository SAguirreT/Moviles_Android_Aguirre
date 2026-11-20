package com.aguirre.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aguirre.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("CentroTareas") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tarea con progreso", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { vm.tareaLargaMala() }) { Text("Tarea larga (MAL)") }
                        Button(onClick = { vm.tareaLarga() }) { Text("Tarea larga (OK)") }
                    }
                    Text(mensaje)
                    // TODO Pasos 2-4: botón funcional, barra de progreso y cancelación.
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Contar números primos", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    Text("Pendiente: Paso 5")
                    // TODO Paso 5: campo numérico, botones Contar/Cancelar y resultado.
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Dos cargas", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = {}, enabled = false) { Text("Una por una") }
                        Button(onClick = {}, enabled = false) { Text("A la vez") }
                    }
                    Text("Pendiente: Pasos 6 y 7")
                    // TODO Pasos 6-7: resultados, indicador de carga, error y reintento.
                }
            }
        }
    }
}
