
package com.aguirre.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.aguirre.campusgps.data.LugaresRepository
import com.aguirre.campusgps.data.UbicacionRepository
import com.aguirre.campusgps.model.Lugar
import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.Posicion
import com.aguirre.campusgps.model.TipoError
import com.aguirre.campusgps.model.UbicacionUi
import com.aguirre.campusgps.util.distanciaMetros

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * MEJORA IA 3:
 * ViewModel con filtros y radio de cercania.
 *
 * Conserva la arquitectura MVVM.
 * No utiliza Context.
 */
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    // Estado de ubicacion.
    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Lista original de lugares.
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { lugar ->
            LugarConDistancia(
                lugar = lugar,
                metros = null
            )
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    // Lugar seleccionado.
    private val _seleccionado =
        MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    // MEJORA 2: categoria activa.
    private val _categoriaSeleccionada =
        MutableStateFlow("Todos")

    val categoriaSeleccionada: StateFlow<String> =
        _categoriaSeleccionada.asStateFlow()

    // MEJORA 3: radio inicial de 200 metros.
    private val _radioMetros =
        MutableStateFlow(200)

    val radioMetros: StateFlow<Int> =
        _radioMetros.asStateFlow()

    // MEJORA 2: lugares filtrados.
    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> =
        combine(
            _lugares,
            _categoriaSeleccionada
        ) { lista, categoria ->

            if (categoria == "Todos") {
                lista
            } else {
                lista.filter { elemento ->
                    elemento.lugar.categoria.equals(
                        categoria,
                        ignoreCase = true
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _lugares.value
        )

    // Lugar mas cercano de la categoria activa.
    val masCercano: StateFlow<Lugar?> =
        lugaresFiltrados.map { lista ->
            lista
                .filter { it.metros != null }
                .minByOrNull {
                    it.metros ?: Float.MAX_VALUE
                }
                ?.lugar
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    /**
     * MEJORA 3:
     * Cantidad de lugares dentro del radio.
     *
     * Se actualiza cuando cambia el filtro,
     * el radio o la ubicacion.
     */
    val cantidadDentroRadio: StateFlow<Int> =
        combine(
            lugaresFiltrados,
            _radioMetros
        ) { lista, radio ->

            lista.count { elemento ->
                val metros = elemento.metros
                metros != null && metros <= radio
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    /**
     * MEJORA 3:
     * Cambiar radio permitido.
     */
    fun cambiarRadio(nuevoRadio: Int) {

        if (nuevoRadio !in listOf(100, 200, 500)) {
            return
        }

        _radioMetros.value = nuevoRadio

        Log.d(
            "CampusGPS-Radio",
            "Radio seleccionado: $nuevoRadio metros"
        )
    }

    /**
     * MEJORA 2:
     * Cambiar categoria.
     */
    fun cambiarCategoria(categoria: String) {

        val permitidas = setOf(
            "Todos",
            "Estudio",
            "Comida",
            "Eventos",
            "Servicios"
        )

        if (categoria !in permitidas) return

        _categoriaSeleccionada.value = categoria

        // Quitar seleccion si ya no pertenece al filtro.
        val idActual = _seleccionado.value

        if (idActual != null) {

            val visible = _lugares.value.any { elemento ->
                elemento.lugar.id == idActual &&
                        (
                                categoria == "Todos" ||
                                        elemento.lugar.categoria.equals(
                                            categoria,
                                            ignoreCase = true
                                        )
                                )
            }

            if (!visible) {
                _seleccionado.value = null
            }
        }
    }

    /**
     * Obtener ubicacion.
     */
    fun buscar() {

        if (!ubicacionRepo.playServicesDisponible()) {

            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services",
                tipo = TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {

            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->

                    actualizarDistancias(posicion)

                    _estado.value =
                        UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->

                    _estado.value = clasificarError(error)
                }
        }
    }

    /**
     * Manejar permiso denegado.
     */
    fun permisoDenegado() {

        _estado.value = UbicacionUi.Error(
            mensaje = "No tienes permiso para acceder a la ubicación",
            tipo = TipoError.SIN_PERMISO
        )
    }

    /**
     * Seleccionar un lugar.
     */
    fun seleccionar(id: Int?) {

        _seleccionado.value = id

        Log.d(
            "CampusGPS-Seleccion",
            "Lugar seleccionado: $id"
        )
    }

    /**
     * Calcular distancias reales.
     */
    private fun actualizarDistancias(pos: Posicion) {

        _lugares.value = lugaresRepo.obtenerLugares()
            .map { lugar ->

                LugarConDistancia(
                    lugar = lugar,
                    metros = distanciaMetros(
                        desde = pos,
                        hasta = lugar
                    )
                )
            }
            .sortedBy {
                it.metros ?: Float.MAX_VALUE
            }
    }

    /**
     * Clasificar errores de ubicacion.
     */
    private fun clasificarError(
        error: Throwable
    ): UbicacionUi.Error {

        return when {

            error is SecurityException -> {
                UbicacionUi.Error(
                    mensaje = "No tienes permiso para acceder a la ubicación",
                    tipo = TipoError.SIN_PERMISO
                )
            }

            error is IllegalStateException &&
                    error.message == "GPS_APAGADO" -> {

                UbicacionUi.Error(
                    mensaje = "Activa el GPS e intenta de nuevo",
                    tipo = TipoError.GPS_APAGADO
                )
            }

            else -> {
                UbicacionUi.Error(
                    mensaje = error.message
                        ?: "No se pudo obtener la ubicación",
                    tipo = TipoError.OTRO
                )
            }
        }
    }
}
