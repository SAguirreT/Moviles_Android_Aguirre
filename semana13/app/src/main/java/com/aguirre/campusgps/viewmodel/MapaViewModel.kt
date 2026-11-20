
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * COMMIT 9:
 * ViewModel de CampusGPS con manejo de errores.
 *
 * Mantiene las funciones de los commits anteriores:
 * - Buscar ubicacion.
 * - Calcular distancias.
 * - Ordenar lugares.
 * - Seleccionar un lugar.
 * - Identificar el mas cercano.
 *
 * No recibe Context.
 */
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    // Estado principal.
    private val _estado = MutableStateFlow<UbicacionUi>(
        UbicacionUi.Inicial
    )

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Lista de lugares.
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
    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    // Lugar mas cercano.
    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista ->
            lista.firstOrNull { it.metros != null }?.lugar
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * COMMIT 9:
     * Comprobar Google Play Services antes de buscar.
     */
    fun buscar() {

        // Comprobar disponibilidad del servicio.
        if (!ubicacionRepo.playServicesDisponible()) {

            _estado.value = UbicacionUi.Error(
                mensaje = "Este dispositivo no tiene Google Play Services",
                tipo = TipoError.SIN_PLAY_SERVICES
            )

            return
        }

        // Iniciar la busqueda.
        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {

            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->

                    // Calcular las distancias.
                    actualizarDistancias(posicion)

                    // Publicar la ubicacion.
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->

                    // Clasificar el error recibido.
                    _estado.value = clasificarError(error)
                }
        }
    }

    /**
     * COMMIT 9:
     * Informar que el usuario rechazo los permisos.
     */
    fun permisoDenegado() {

        _estado.value = UbicacionUi.Error(
            mensaje = "No tienes permiso para acceder a la ubicación",
            tipo = TipoError.SIN_PERMISO
        )
    }

    /**
     * COMMIT 7:
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
     * COMMIT 6:
     * Calcular distancias y ordenar los lugares.
     */
    private fun actualizarDistancias(pos: Posicion) {

        val lugaresCalculados = lugaresRepo.obtenerLugares()
            .map { lugar ->

                val metros = distanciaMetros(
                    desde = pos,
                    hasta = lugar
                )

                LugarConDistancia(
                    lugar = lugar,
                    metros = metros
                )
            }
            .sortedBy { it.metros }

        // Publicar los lugares ordenados.
        _lugares.value = lugaresCalculados

        lugaresCalculados.forEach { elemento ->

            Log.d(
                "CampusGPS-Distancias",
                "${elemento.lugar.nombre}: ${elemento.metros} metros"
            )
        }
    }

    /**
     * COMMIT 9:
     * Convertir excepciones en errores de la interfaz.
     */
    private fun clasificarError(error: Throwable): UbicacionUi.Error {

        return when {

            // El usuario no concedio permisos.
            error is SecurityException -> {

                UbicacionUi.Error(
                    mensaje = "No tienes permiso para acceder a la ubicación",
                    tipo = TipoError.SIN_PERMISO
                )
            }

            // GPS desactivado o ubicacion no disponible.
            error is IllegalStateException &&
                    error.message == "GPS_APAGADO" -> {

                UbicacionUi.Error(
                    mensaje = "Activa el GPS e intenta de nuevo",
                    tipo = TipoError.GPS_APAGADO
                )
            }

            // Otros errores.
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
