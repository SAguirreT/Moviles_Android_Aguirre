package com.tecsup.laptops.data.repository

import com.tecsup.laptops.data.local.ItemDao
import com.tecsup.laptops.data.mapper.toDomain
import com.tecsup.laptops.data.mapper.toEntity
import com.tecsup.laptops.data.remote.ApiService
import com.tecsup.laptops.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getLaptops(): Flow<List<Item>> {
        return itemDao.getLaptops().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavoritos(): Flow<List<Item>> {
        return itemDao.getFavoritos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshLaptops(): Result<Int> {
        return try {
            val favIds = itemDao.getFavoritoIds().toSet()
            val response = apiService.getLaptops()
            val entities = response.products.map { dto ->
                dto.toEntity(esFavorito = favIds.contains(dto.id))
            }
            itemDao.insertLaptops(entities)
            Result.success(entities.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLaptopById(id: Int): Item? {
        val entity = itemDao.getLaptopById(id)
        if (entity != null) {
            return entity.toDomain()
        }
        return try {
            val dto = apiService.getLaptopById(id)
            dto.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateFavorito(id: Int, esFavorito: Boolean) {
        itemDao.updateFavorito(id, esFavorito)
    }
}
