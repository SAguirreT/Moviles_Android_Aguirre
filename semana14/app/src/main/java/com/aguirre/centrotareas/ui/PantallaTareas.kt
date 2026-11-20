package com.aguirre.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aguirre.centrotareas.model.CargaRecord
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import com.aguirre.centrotareas.viewmodel.TareasViewModel

/**
 * Pantalla principal profesional con navegación por pestañas (Tabs) que separa
 * limpiamente las etapas del Laboratorio 14 de acuerdo con el diseño de referencia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Hilos y Progreso", "Números Primos", "Gestión de Cargas")

    // Estados observados desde el ViewModel
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    val primosUi by vm.primos.collectAsState()
    val cargaUi by vm.carga.collectAsState()
    val historial by vm.historialCargas.collectAsState()
    var limiteInput by remember { mutableStateOf("500000") }
    var simularError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("CentroTareas", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, style = MaterialTheme.typography.bodyMedium) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> VistaTareaProgreso(
                    toques = toques,
                    onToque = { toques++ },
                    mensaje = mensaje,
                    progreso = progreso,
                    corriendo = corriendo,
                    onTareaMala = { vm.tareaLargaMala() },
                    onTareaBuena = { vm.tareaLarga() },
                    onCancelar = { vm.cancelarTarea() }
                )
                1 -> VistaPrimos(
                    limiteInput = limiteInput,
                    onLimiteChange = { limiteInput = it },
                    primosUi = primosUi,
                    onContar = { vm.contar(limiteInput.toIntOrNull() ?: 500000) },
                    onCancelar = { vm.cancelarPrimos() }
                )
                2 -> VistaCargas(
                    simularError = simularError,
                    onSimularErrorChange = { simularError = it },
                    cargaUi = cargaUi,
                    historial = historial,
                    onCargarSecuencial = { vm.cargarUnaPorUna(simularError) },
                    onCargarParalela = { vm.cargarALaVez(simularError) },
                    onCargarReintentos = { vm.cargarConReintentos(simularError) },
                    onReintentarSinError = {
                        simularError = false
                        vm.cargarALaVez(false)
                    }
                )
            }
        }
    }
}

@Composable
fun VistaTareaProgreso(
    toques: Int,
    onToque: () -> Unit,
    mensaje: String,
    progreso: Float,
    corriendo: Boolean,
    onTareaMala: () -> Unit,
    onTareaBuena: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Demostración de Hilos y Corrutinas",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Button(
                    onClick = onToque,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Contador interactivo (Tócame: $toques)")
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Seleccione el tipo de ejecución:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onTareaMala,
                        enabled = !corriendo,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Bloqueante (MAL)")
                    }
                    Button(
                        onClick = onTareaBuena,
                        enabled = !corriendo,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("No bloqueante (OK)")
                    }
                }

                if (corriendo) {
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar Tarea")
                    }
                }

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Text(
                    text = "Progreso: ${(progreso * 100).toInt()}% • Estado: $mensaje",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun VistaPrimos(
    limiteInput: String,
    onLimiteChange: (String) -> Unit,
    primosUi: PrimosUi,
    onContar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Cálculo de Números Primos",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = limiteInput,
                    onValueChange = onLimiteChange,
                    label = { Text("Límite superior (ej. 500000)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onContar,
                        enabled = primosUi !is PrimosUi.Calculando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Iniciar Conteo")
                    }
                    OutlinedButton(
                        onClick = onCancelar,
                        enabled = primosUi is PrimosUi.Calculando,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }

                Spacer(Modifier.height(4.dp))

                when (val estado = primosUi) {
                    is PrimosUi.Inicial -> Text("Ingrese un límite y presione Iniciar Conteo.")
                    is PrimosUi.Calculando -> Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Calculando primos en Dispatchers.Default...")
                    }
                    is PrimosUi.Listo -> Text("Resultado: ${estado.cantidad} primos encontrados en ${estado.milisegundos} ms.")
                    is PrimosUi.Cancelado -> Text("Cálculo cancelado por el usuario.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun VistaCargas(
    simularError: Boolean,
    onSimularErrorChange: (Boolean) -> Unit,
    cargaUi: CargaUi,
    historial: List<CargaRecord>,
    onCargarSecuencial: () -> Unit,
    onCargarParalela: () -> Unit,
    onCargarReintentos: () -> Unit,
    onReintentarSinError: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Gestión de Cargas y Red",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = simularError,
                        onCheckedChange = onSimularErrorChange
                    )
                    Text("Simular fallo de red (IOException)")
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCargarSecuencial,
                        enabled = cargaUi !is CargaUi.Cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Carga Secuencial (Una por una)")
                    }
                    Button(
                        onClick = onCargarParalela,
                        enabled = cargaUi !is CargaUi.Cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Carga en Paralelo (A la vez)")
                    }
                    OutlinedButton(
                        onClick = onCargarReintentos,
                        enabled = cargaUi !is CargaUi.Cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Carga con Reintentos Automáticos")
                    }
                }

                Spacer(Modifier.height(4.dp))

                when (val estado = cargaUi) {
                    is CargaUi.Inicial -> Text("Seleccione un método de carga para iniciar.")
                    is CargaUi.Cargando -> Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Procesando peticiones asíncronas...")
                    }
                    is CargaUi.Exito -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Modo: ${estado.modo}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Text("Perfil: ${estado.perfil}")
                        Text("Pedidos: ${estado.pedidos}")
                        Text("Duración total: ${estado.milisegundos} ms", color = MaterialTheme.colorScheme.secondary)
                    }
                    is CargaUi.Error -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Error: ${estado.mensaje}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = onReintentarSinError) {
                            Text("Reintentar sin error")
                        }
                    }
                }

                if (historial.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text("Historial Comparativo de Tiempos:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    for (rec in historial) {
                        Text("• ${rec.modo}: ${rec.milisegundos} ms", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
