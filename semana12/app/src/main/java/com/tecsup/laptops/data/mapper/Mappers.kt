package com.tecsup.laptops.data.mapper

import com.tecsup.laptops.data.remote.dto.ItemDto
import com.tecsup.laptops.domain.model.Item

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