package com.aguirre.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguirre.centrotareas.data.TareasRepository
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
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

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var tareaJob: Job? = null

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
     * Pasos 2, 3 y 4: Tarea larga controlada con Job, corrutina y capacidad de cancelacion.
     * Mantiene una referencia al Job asignado y comprueba la cancelacion activa.
     */
    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        _progreso.value = 0f

        tareaJob = viewModelScope.launch {
            _mensaje.value = "Iniciando tarea..."
            try {
                withContext(Dispatchers.Default) {
                    for (i in 1..10) {
                        ensureActive() // Verifica si el Job ha sido cancelado
                        delay(500)
                        _progreso.value = i / 10f
                        _mensaje.value = "Procesando paso $i de 10..."
                    }
                }
                _mensaje.value = "Tarea finalizada con exito"
            } catch (e: CancellationException) {
                _mensaje.value = "Tarea cancelada por el usuario"
                _progreso.value = 0f
            } finally {
                _corriendo.value = false
            }
        }
    }

    /**
     * Paso 4: Cancela el Job en ejecucion de forma inmediata.
     */
    fun cancelarTarea() {
        tareaJob?.cancel()
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var primosJob: Job? = null

    /**
     * Paso 5: Inicia el conteo de numeros primos de forma asincrona en Dispatchers.Default.
     * Mide el tiempo transcurrido en milisegundos y captura la cancelacion por parte del usuario.
     */
    fun contar(hasta: Int) {
        primosJob?.cancel()
        _primos.value = PrimosUi.Calculando
        val inicio = System.currentTimeMillis()

        primosJob = viewModelScope.launch {
            try {
                val cantidad = repo.contarPrimos(hasta)
                val duracion = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, duracion)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
            } catch (e: Exception) {
                _primos.value = PrimosUi.Inicial
            }
        }
    }

    /**
     * Paso 5: Cancela el calculo de numeros primos en ejecucion.
     */
    fun cancelarPrimos() {
        primosJob?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()
    // TODO Paso 6: fun cargarUnaPorUna() y fun cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}
