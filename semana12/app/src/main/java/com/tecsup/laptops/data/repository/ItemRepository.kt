package com.tecsup.laptops.data.repository

import com.tecsup.laptops.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getLaptops(): Flow<List<Item>>
    fun getFavoritos(): Flow<List<Item>>
    suspend fun refreshLaptops(): Result<Int>
    suspend fun getLaptopById(id: Int): Item?
    suspend fun updateFavorito(id: Int, esFavorito: Boolean)
}
