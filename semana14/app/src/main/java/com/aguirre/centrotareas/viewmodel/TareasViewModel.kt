package com.aguirre.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguirre.centrotareas.data.TareasRepository
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    /**
     * Paso 1: Tarea pesada que bloquea el hilo principal (Main Thread).
     * Utiliza Thread.sleep en el hilo principal provocando congelamiento de la UI.
     */
    fun tareaLargaMala() {
        _mensaje.value = "Iniciando tarea pesada en Main Thread..."
        _progreso.value = 0f
        for (i in 1..5) {
            Thread.sleep(1000)
            _progreso.value = i / 5f
            _mensaje.value = "Procesando segundo $i de 5..."
        }
        _mensaje.value = "Tarea finalizada (con bloqueo de UI)"
    }

    /**
     * Paso 2 y 3: Tarea larga no bloqueante con actualizacion progresiva en StateFlow.
     * Mueve el trabajo a corrutina y publica el progreso (0.0 a 1.0) en cada iteracion.
     */
    fun tareaLarga() {
        viewModelScope.launch {
            _mensaje.value = "Iniciando tarea en corrutina..."
            _progreso.value = 0f
            withContext(Dispatchers.Default) {
                for (i in 1..10) {
                    delay(500) // 10 pasos de 500ms (5 segundos en total)
                    _progreso.value = i / 10f
                    _mensaje.value = "Procesando paso $i de 10..."
                }
            }
            _mensaje.value = "Tarea finalizada sin congelar UI"
        }
    }
    // TODO Paso 4: trabajo (Job), corriendo y cancelar()

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    // TODO Paso 5: fun contar(hasta: Int) y fun cancelarPrimos()

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()
    // TODO Paso 6: fun cargarUnaPorUna() y fun cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}
