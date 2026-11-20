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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aguirre.centrotareas.model.PrimosUi
import com.aguirre.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    val primosUi by vm.primos.collectAsState()
    var limiteInput by remember { mutableStateOf("500000") }

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
                        Button(onClick = { vm.tareaLargaMala() }, enabled = !corriendo) { Text("Tarea larga (MAL)") }
                        Button(onClick = { vm.tareaLarga() }, enabled = !corriendo) { Text("Tarea larga (OK)") }
                        OutlinedButton(onClick = { vm.cancelarTarea() }, enabled = corriendo) { Text("Cancelar") }
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("${(progreso * 100).toInt()}% - $mensaje")
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Contar números primos", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                    
                    OutlinedTextField(
                        value = limiteInput,
                        onValueChange = { limiteInput = it },
                        label = { Text("Límite (ej. 500000)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val hasta = limiteInput.toIntOrNull() ?: 500000
                                vm.contar(hasta)
                            },
                            enabled = primosUi !is PrimosUi.Calculando
                        ) {
                            Text("Contar")
                        }
                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = primosUi is PrimosUi.Calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primosUi) {
                        is PrimosUi.Inicial -> Text("Ingresa un número para contar primos")
                        is PrimosUi.Calculando -> Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.height(24.dp).padding(end = 8.dp))
                            Text("Calculando números primos...")
                        }
                        is PrimosUi.Listo -> Text("Se encontraron ${estado.cantidad} primos en ${estado.milisegundos} ms.")
                        is PrimosUi.Cancelado -> Text("Cálculo cancelado por el usuario.", color = MaterialTheme.colorScheme.error)
                    }
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
