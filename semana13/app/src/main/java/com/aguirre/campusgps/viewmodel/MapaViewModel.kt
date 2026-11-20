
package com.aguirre.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.aguirre.campusgps.data.LugaresRepository
import com.aguirre.campusgps.data.UbicacionRepository
import com.aguirre.campusgps.model.Lugar
import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.Posicion
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
 * COMMIT 8:
 * Gestionar ubicacion, distancias, seleccion
 * y deteccion del lugar mas cercano.
 */
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    // Estado de la ubicacion actual.
    private val _estado = MutableStateFlow<UbicacionUi>(
        UbicacionUi.Inicial
    )

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Cargar inicialmente los lugares sin distancia.
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

    // Lugar seleccionado en la lista.
    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    /**
     * COMMIT 8:
     * Obtener automaticamente el lugar mas cercano.
     *
     * Como la lista esta ordenada por distancia,
     * el primer lugar con metros calculados es el mas cercano.
     *
     * Antes de obtener la ubicacion, devuelve null.
     */
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
     * Obtener la ubicacion actual del usuario.
     */
    fun buscar() {

        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {

            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->

                    // Calcular y ordenar las distancias.
                    actualizarDistancias(posicion)

                    // Actualizar la ubicacion actual.
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->

                    _estado.value = UbicacionUi.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    /**
     * Informar cuando se rechaza el permiso.
     */
    fun permisoDenegado() {

        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    /**
     * COMMIT 7:
     * Seleccionar un lugar mediante su identificador.
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
     * Calcular y ordenar las distancias a los lugares.
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

        // Publicar la lista ordenada.
        _lugares.value = lugaresCalculados

        // Mostrar las distancias en Logcat.
        lugaresCalculados.forEach { elemento ->

            Log.d(
                "CampusGPS-Distancias",
                "${elemento.lugar.nombre}: ${elemento.metros} metros"
            )
        }
    }

    /**
     * Convertir los errores tecnicos en mensajes claros.
     */
    private fun mensajeDe(error: Throwable): String {

        return when (error) {

            is SecurityException ->
                "Falta el permiso de ubicación"

            else ->
                error.message ?: "No se pudo obtener la ubicación"
        }
    }
}
