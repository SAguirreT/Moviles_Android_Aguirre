package com.tecsup.murocampus.viewmodel

sealed interface UiState<out T> {
    object Cargando : UiState<Nothing>
    data class Exito<T>(val data: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
