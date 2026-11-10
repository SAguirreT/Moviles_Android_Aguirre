package com.tecsup.laptops.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laptops")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val marca: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)