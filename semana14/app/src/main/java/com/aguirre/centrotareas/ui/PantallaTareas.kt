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
import com.aguirre.centrotareas.model.CargaRecord
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import com.aguirre.centrotareas.viewmodel.TareasViewModel

/**
 * Pantalla principal que respeta estrictamente el diseño oficial del Laboratorio 14:
 * Una única pantalla con desplazamiento vertical y tres tarjetas organizadas:
 * 1. Tarea con progreso (con botones Iniciar/Cancelar, barra de porcentaje y estado).
 * 2. Contar números primos (con campo numérico, recuadro verde de éxito y control de cálculo).
 * 3. Dos cargas (con simulación de errores, recuadros verde/rojo para resultados/errores e historial).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    // Estados observables desde el ViewModel y variables locales de entrada
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    val primosUi by vm.primos.collectAsState()
    val cargaUi by vm.carga.collectAsState()
    val historial by vm.historialCargas.collectAsState()
    
    // Valor inicial de 500000 para el cálculo de números primos
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
                    Text(
                        text = "Tarea con progreso",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { toques++ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tócame: $toques")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo, // Iniciar se deshabilita mientras se ejecuta
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { vm.cancelarTarea() },
                            enabled = corriendo, // Cancelar solo se habilita durante la ejecución
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

                    // Porcentaje debajo de la barra
                    Text(
                        text = "${(progreso * 100).toInt()}%",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Estado debajo del porcentaje (conserva el último valor al cancelar)
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

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

                    // Renderizado de estados del cálculo de primos
                    when (val estado = primosUi) {
                        is PrimosUi.Inicial -> Text("Ingrese un límite numérico para iniciar el conteo.")
                        is PrimosUi.Calculando -> Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Text("Calculando primos en hilo secundario...")
                        }
                        is PrimosUi.Listo -> {
                            // Recuadro verde para resultado exitoso
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("¡Cálculo finalizado con éxito!", style = MaterialTheme.typography.titleSmall, color = Color(0xFF2E7D32))
                                    Text("• Cantidad de primos: ${estado.cantidad}", color = Color(0xFF1B5E20))
                                    Text("• Tiempo transcurrido: ${estado.milisegundos} ms", color = Color(0xFF1B5E20))
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = simularError,
                            onCheckedChange = { simularError = it }
                        )
                        Text("Simular error")
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

                    // Renderizado de estados de carga (éxito, error, cargando)
                    when (val estado = cargaUi) {
                        is CargaUi.Inicial -> Text("Seleccione un modo de carga.")
                        is CargaUi.Cargando -> Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Text("Cargando perfil y pedidos...")
                        }
                        is CargaUi.Exito -> {
                            // Recuadro verde para éxito
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Modo: ${estado.modo}", style = MaterialTheme.typography.titleSmall, color = Color(0xFF2E7D32))
                                    Text("• Perfil: ${estado.perfil}", color = Color(0xFF1B5E20))
                                    Text("• Pedidos: ${estado.pedidos}", color = Color(0xFF1B5E20))
                                    Text("• Tiempo: ${estado.milisegundos} ms", color = Color(0xFF1B5E20))
                                }
                            }
                        }
                        is CargaUi.Error -> {
                            // Recuadro rojo claro para errores
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
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }

                    // Historial comparativo de tiempos
                    if (historial.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(4.dp))
                        Text("Historial Comparativo:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        for (rec in historial) {
                            Text("• ${rec.modo}: ${rec.milisegundos} ms", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
