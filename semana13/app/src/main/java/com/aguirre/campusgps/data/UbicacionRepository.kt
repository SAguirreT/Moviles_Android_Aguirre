package com.aguirre.campusgps.data
import com.aguirre.campusgps.model.Posicion
interface UbicacionRepository { suspend fun obtenerPosicion(): Result<Posicion> }
