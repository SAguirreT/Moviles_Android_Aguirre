package com.tecsup.laptops.domain.model

data class Item(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val marca: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)