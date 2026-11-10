package com.tecsup.laptops.data.auth

import com.tecsup.laptops.domain.model.Sesion
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val estadoSesion: Flow<Sesion>
    suspend fun iniciarSesion(email: String, clave: String): Result<Unit>
    suspend fun cerrarSesion()
}