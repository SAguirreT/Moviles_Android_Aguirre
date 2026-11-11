package com.tecsup.laptops.data.mapper

import com.tecsup.laptops.data.local.ItemEntity
import com.tecsup.laptops.data.remote.dto.ItemDto
import com.tecsup.laptops.domain.model.Item

fun ItemDto.toEntity(esFavorito: Boolean = false): ItemEntity {
    return ItemEntity(
        id = this.id,
        titulo = this.title,
        descripcion = this.description,
        precio = this.price,
        marca = this.brand ?: "Sin marca",
        imagenUrl = this.thumbnail,
        esFavorito = esFavorito
    )
}

fun ItemEntity.toDomain(): Item {
    return Item(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        precio = this.precio,
        marca = this.marca,
        imagenUrl = this.imagenUrl,
        esFavorito = this.esFavorito
    )
}

fun ItemDto.toDomain(): Item {
    return Item(
        id = this.id,
        titulo = this.title,
        descripcion = this.description,
        precio = this.price,
        marca = this.brand ?: "Sin marca",
        imagenUrl = this.thumbnail,
        esFavorito = false
    )
}
