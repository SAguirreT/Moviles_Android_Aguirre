package com.aguirre.campusgps.data
import android.annotation.SuppressLint
import android.content.Context
import com.aguirre.campusgps.model.Posicion
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    private val cliente = LocationServices.getFusedLocationProviderClient(context.applicationContext)
    @SuppressLint("MissingPermission") // El permiso se verifica en la pantalla en el commit 3.
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val ubicacion = cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            ?: error("No se pudo obtener la ubicación")
        Posicion(ubicacion.latitude, ubicacion.longitude)
    }
}
