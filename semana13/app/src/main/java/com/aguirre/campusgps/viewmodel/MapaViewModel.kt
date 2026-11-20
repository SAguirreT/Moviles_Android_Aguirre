
package com.aguirre.campusgps.viewmodel

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.aguirre.campusgps.data.LugaresRepository
import com.aguirre.campusgps.data.UbicacionRepository

import com.aguirre.campusgps.model.LugarConDistancia
import com.aguirre.campusgps.model.Posicion
import com.aguirre.campusgps.model.UbicacionUi

import com.aguirre.campusgps.util.distanciaMetros

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * COMMIT 6:
 * ViewModel principal de CampusGPS.
 *
 * Responsabilidades:
 * - Obtener la ubicacion desde el Repository.
 * - Mantener el estado de la pantalla.
 * - Calcular la distancia hacia cada lugar.
 * - Ordenar los lugares del mas cercano al mas lejano.
 * - Publicar la lista actualizada mediante StateFlow.
 *
 * No utiliza Composables ni recibe Context.
 */
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    // Estado actual de la ubicacion.
    private val _estado = MutableStateFlow<UbicacionUi>(
        UbicacionUi.Inicial
    )

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Cargar inicialmente los cinco lugares sin distancias.
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

    // Preparar el estado de seleccion para el Commit 7.
    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    /**
     * Buscar la ubicacion actual del usuario.
     *
     * Si se obtiene correctamente, actualizar las distancias.
     * Si ocurre un error, publicar el mensaje correspondiente.
     */
    fun buscar() {

        // Indicar que la ubicacion esta siendo consultada.
        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {

            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->

                    // COMMIT 6:
                    // Calcular las distancias cuando la ubicacion
                    // se obtiene correctamente.
                    actualizarDistancias(posicion)

                    // Publicar la ubicacion obtenida.
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->

                    // Mostrar el error sin cerrar la aplicacion.
                    _estado.value = UbicacionUi.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    /**
     * Informar que el usuario rechazo los permisos.
     */
    fun permisoDenegado() {

        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    /**
     * COMMIT 6:
     * Calcular y ordenar las distancias a los cinco lugares.
     *
     * Este metodo es privado porque pertenece
     * a la logica interna del ViewModel.
     */
    private fun actualizarDistancias(pos: Posicion) {

        // Obtener los lugares del Repository.
        val lugaresOriginales = lugaresRepo.obtenerLugares()

        // Calcular la distancia de cada lugar.
        val lugaresCalculados = lugaresOriginales.map { lugar ->

            val metros = distanciaMetros(
                desde = pos,
                hasta = lugar
            )

            LugarConDistancia(
                lugar = lugar,
                metros = metros
            )
        }

        // Ordenar del lugar mas cercano al mas lejano.
        val lugaresOrdenados = lugaresCalculados.sortedBy {
            it.metros
        }

        // Publicar los resultados para que la UI los observe.
        _lugares.value = lugaresOrdenados

        // Mostrar las distancias en Logcat para verificar
        // el funcionamiento antes del Commit 7.
        lugaresOrdenados.forEach { elemento ->

            Log.d(
                "CampusGPS-Distancias",
                "${elemento.lugar.nombre}: ${elemento.metros} metros"
            )
        }
    }

    /**
     * Convertir los errores tecnicos en mensajes comprensibles.
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
