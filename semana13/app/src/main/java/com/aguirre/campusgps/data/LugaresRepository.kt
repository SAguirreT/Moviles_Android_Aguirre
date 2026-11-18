package com.aguirre.campusgps.data
import com.aguirre.campusgps.model.Lugar
interface LugaresRepository { fun obtenerLugares(): List<Lugar> }
