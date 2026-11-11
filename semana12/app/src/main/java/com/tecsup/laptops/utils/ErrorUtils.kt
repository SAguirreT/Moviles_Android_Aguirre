package com.tecsup.laptops.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

fun Throwable.toUserFriendlyMessage(): String {
    if (this is CancellationException) throw this
    return when (this) {
        is SocketTimeoutException -> "El servidor tardó demasiado en responder"
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${this.code()}"
        else -> this.localizedMessage?.ifBlank { "Ocurrió un error inesperado" } ?: "Ocurrió un error inesperado"
    }
}
