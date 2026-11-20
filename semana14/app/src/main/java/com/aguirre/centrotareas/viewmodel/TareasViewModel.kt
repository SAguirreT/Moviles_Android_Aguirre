package com.aguirre.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguirre.centrotareas.data.TareasRepository
import com.aguirre.centrotareas.model.CargaUi
import com.aguirre.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
     * Mejora 1 (Commit 10): Tarea larga que consume el flujo de progreso emitido por el Repository mediante Flow.
     */
    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        _progreso.value = 0f

        tareaJob = viewModelScope.launch {
            _mensaje.value = "Iniciando tarea con Repository Flow..."
            try {
                repo.observarProgreso(10).collect { p ->
                    _progreso.value = p
                    _mensaje.value = "Procesando ${(p * 100).toInt()}%..."
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

    /**
     * Paso 6: Realiza la carga de perfil y pedidos de forma secuencial (una por una).
     * Tiempo aproximado total: 2s (perfil) + 3s (pedidos) = 5s.
     */
    fun cargarUnaPorUna(simularError: Boolean = false) {
        _carga.value = CargaUi.Cargando
        val inicio = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(simularError)
                val tiempoTotal = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Secuencial", perfil, pedidos.size, tiempoTotal)
            } catch (e: Exception) {
                _carga.value = CargaUi.Error(e.message ?: "Error al cargar datos")
            }
        }
    }

    /**
     * Paso 6: Realiza la carga de perfil y pedidos de forma paralela utilizando async en coroutineScope.
     * Tiempo aproximado total: max(2s, 3s) = 3s.
     */
    fun cargarALaVez(simularError: Boolean = false) {
        _carga.value = CargaUi.Cargando
        val inicio = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                coroutineScope {
                    val defPerfil = async { repo.cargarPerfil() }
                    val defPedidos = async { repo.cargarPedidos(simularError) }
                    val perfil = defPerfil.await()
                    val pedidos = defPedidos.await()
                    val tiempoTotal = System.currentTimeMillis() - inicio
                    _carga.value = CargaUi.Exito("En paralelo", perfil, pedidos.size, tiempoTotal)
                }
            } catch (e: Exception) {
                _carga.value = CargaUi.Error(e.message ?: "Error al cargar datos")
            }
        }
    }

    /**
     * Mejora 2 (Commit 11): Carga de datos con reintentos automáticos y espera progresiva (backoff exponencial).
     * Intenta hasta 3 veces esperando 1s, luego 2s entre reintentos en caso de fallo.
     */
    fun cargarConReintentos(simularError: Boolean = false) {
        _carga.value = CargaUi.Cargando
        val inicio = System.currentTimeMillis()
        viewModelScope.launch {
            var intentos = 0
            val maxIntentos = 3
            var exito = false
            var perfil = ""
            var pedidos = emptyList<String>()
            var ultimoError: Exception? = null

            while (intentos < maxIntentos && !exito) {
                try {
                    intentos++
                    coroutineScope {
                        val defPerfil = async { repo.cargarPerfil() }
                        val defPedidos = async { repo.cargarPedidos(simularError) }
                        perfil = defPerfil.await()
                        pedidos = defPedidos.await()
                    }
                    exito = true
                } catch (e: Exception) {
                    ultimoError = e
                    if (intentos < maxIntentos) {
                        delay(intentos * 1000L) // Espera progresiva: 1s, 2s
                    }
                }
            }

            val tiempoTotal = System.currentTimeMillis() - inicio
            if (exito) {
                _carga.value = CargaUi.Exito("Reintentos ($intentos)", perfil, pedidos.size, tiempoTotal)
            } else {
                _carga.value = CargaUi.Error(ultimoError?.message ?: "Falló tras $maxIntentos intentos")
            }
        }
    }
}
