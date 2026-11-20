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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import com.aguirre.centrotareas.viewmodel.TareasViewModel

/**
 * Pantalla principal del Laboratorio 14 — CentroTareas que reproduce fielmente
 * las capturas oficiales: barra superior azul, fondo claro, tarjetas blancas compactas
 * con títulos alineados a la izquierda y botones centrados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    // Estados observables desde el ViewModel
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    val primosUi by vm.primos.collectAsState()
    val cargaUi by vm.carga.collectAsState()
    val historial by vm.historialCargas.collectAsState()

    // Parámetros de entrada para primos y simulación de error
    var limiteInput by remember { mutableStateOf("500000") }
    var simularError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFC))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==========================================
            // TARJETA 1: Tarea con progreso
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Título alineado a la izquierda
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Tarea con progreso",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    // Botón Tócame centrado
                    Button(
                        onClick = { toques++ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tócame: $toques")
                    }

                    // Fila de botones Iniciar / Cancelar centrados
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo, // Iniciar activo solo cuando no corre
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { vm.cancelarTarea() },
                            enabled = corriendo, // Cancelar activo solo durante la ejecución
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Barra de progreso horizontal
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    // Porcentaje centrado debajo de la barra
                    Text(
                        text = "${(progreso * 100).toInt()}%",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Estado centrado debajo del porcentaje (conserva último valor al cancelar)
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ==========================================
            // TARJETA 2: Contar números primos
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Título alineado a la izquierda
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Contar números primos",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    OutlinedTextField(
                        value = limiteInput,
                        onValueChange = { limiteInput = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val hasta = limiteInput.toIntOrNull() ?: 500000
                                vm.contar(hasta)
                            },
                            enabled = primosUi !is PrimosUi.Calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = primosUi is PrimosUi.Calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Renderizado de estados para primos
                    when (val estado = primosUi) {
                        is PrimosUi.Inicial -> Text("Ingrese un límite numérico para iniciar el conteo.")
                        is PrimosUi.Calculando -> Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Text("Calculando primos en Dispatchers.Default...")
                        }
                        is PrimosUi.Listo -> {
                            // Recuadro verde claro para el resultado exitoso
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Primos hasta $limiteInput: ${estado.cantidad}", style = MaterialTheme.typography.titleSmall, color = Color(0xFF2E7D32))
                                    Text("Tiempo: ${estado.milisegundos} ms", color = Color(0xFF1B5E20))
                                }
                            }
                        }
                        is PrimosUi.Cancelado -> Text("Cálculo cancelado por el usuario.", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            // ==========================================
            // TARJETA 3: Dos cargas
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Título alineado a la izquierda
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Dos cargas",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    // Switch azul con etiqueta Simular error
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simular error", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = simularError,
                            onCheckedChange = { simularError = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { vm.cargarUnaPorUna(simularError = simularError) },
                            enabled = cargaUi !is CargaUi.Cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = { vm.cargarALaVez(simularError = simularError) },
                            enabled = cargaUi !is CargaUi.Cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("A la vez")
                        }
                    }

                    OutlinedButton(
                        onClick = { vm.cargarConReintentos(simularError = simularError) },
                        enabled = cargaUi !is CargaUi.Cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reintentos automáticos")
                    }

                    Spacer(Modifier.height(4.dp))

                    // Renderizado de estados para cargas
                    when (val estado = cargaUi) {
                        is CargaUi.Inicial -> Text("Seleccione un modo de carga.")
                        is CargaUi.Cargando -> Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Text("Procesando solicitudes asíncronas...")
                        }
                        is CargaUi.Exito -> {
                            // Recuadro verde claro para éxito
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Modo: ${estado.modo}", style = MaterialTheme.typography.titleSmall, color = Color(0xFF2E7D32))
                                    Text("Perfil: ${estado.perfil}", color = Color(0xFF1B5E20))
                                    Text("Pedidos: ${estado.pedidos}", color = Color(0xFF1B5E20))
                                    Text("Tiempo: ${estado.milisegundos} ms", color = Color(0xFF1B5E20))
                                }
                            }
                        }
                        is CargaUi.Error -> {
                            // Recuadro rojo claro para errores con botón Reintentar
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Error: ${estado.mensaje}", color = Color(0xFFC62828), style = MaterialTheme.typography.bodyMedium)
                                    Button(
                                        onClick = {
                                            simularError = false
                                            vm.cargarALaVez(simularError = false)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }

                    // Historial comparativo de tiempos (sección secundaria en mejora-ia)
                    if (historial.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Historial Comparativo:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }
                        for (rec in historial) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "• ${rec.modo}: ${rec.milisegundos} ms",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
