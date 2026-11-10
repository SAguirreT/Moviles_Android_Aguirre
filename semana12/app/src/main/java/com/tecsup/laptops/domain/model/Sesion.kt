package com.tecsup.laptops.domain.model

enum class Rol {
    ADMIN,
    USUARIO
}

sealed interface Sesion {
    data object Inactiva : Sesion
    data class Activa(
        val uid: String,
        val email: String,
        val rol: Rol = Rol.USUARIO
    ) : Sesion
}