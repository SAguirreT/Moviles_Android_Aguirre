
package com.aguirre.campusgps.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager

import androidx.core.content.ContextCompat

import com.aguirre.campusgps.model.Posicion

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import kotlinx.coroutines.tasks.await

/**
 * COMMIT 9:
 * Implementacion del repositorio de ubicacion.
 *
 * Responsabilidades:
 * - Comprobar permisos.
 * - Consultar Google Play Services.
 * - Detectar ubicacion desactivada o no disponible.
 * - Obtener coordenadas mediante FusedLocationProviderClient.
 *
 * Context solo se utiliza dentro de la capa Repository.
 */
class UbicacionRepositoryGoogle(
    private val context: Context
) : UbicacionRepository {

    // Cliente de ubicacion de Google.
    private val cliente =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Comprobar si Google Play Services esta disponible.
     */
    override fun playServicesDisponible(): Boolean {

        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) ==
                ConnectionResult.SUCCESS
    }

    /**
     * Obtener la ubicacion actual.
     */
    override suspend fun obtenerPosicion(): Result<Posicion> {

        return try {

            // Comprobar los permisos.
            val permisoFino = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val permisoAproximado = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!permisoFino && !permisoAproximado) {
                throw SecurityException(
                    "No se concedieron permisos de ubicación"
                )
            }

            // Comprobar que la ubicacion del dispositivo este activada.
            val locationManager = context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

            if (!locationManager.isLocationEnabled) {
                throw IllegalStateException(
                    "GPS_APAGADO"
                )
            }

            // Solicitar una ubicacion actual al proveedor de Google.
            val ubicacion = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()

            // El proveedor puede devolver null si no obtiene posicion.
            if (ubicacion == null) {
                throw IllegalStateException(
                    "GPS_APAGADO"
                )
            }

            // Convertir la respuesta a nuestro modelo.
            Result.success(
                Posicion(
                    latitud = ubicacion.latitude,
                    longitud = ubicacion.longitude
                )
            )

        } catch (error: Exception) {

            // Devolver el error al ViewModel.
            Result.failure(error)
        }
    }
}
