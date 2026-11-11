package com.tecsup.laptops.data.repository

import com.tecsup.laptops.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>
    fun getLaptops(): Flow<List<Item>> = items
    fun getFavoritos(): Flow<List<Item>>
    suspend fun refrescar(): Result<Int>
    suspend fun refreshLaptops(): Result<Int> = refrescar()
    fun cantidadGuardada(): Int
    suspend fun alternarFavorito(id: Int)
    suspend fun updateFavorito(id: Int, esFavorito: Boolean) { alternarFavorito(id) }
    suspend fun getLaptopById(id: Int): Item?
    suspend fun limpiarCache()
}
