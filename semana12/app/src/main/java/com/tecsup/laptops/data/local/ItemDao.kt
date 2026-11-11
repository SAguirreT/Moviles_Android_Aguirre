package com.tecsup.laptops.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM laptops")
    fun getLaptops(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM laptops WHERE esFavorito = 1")
    fun getFavoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM laptops WHERE esFavorito = 1")
    suspend fun getFavoritoIds(): List<Int>

    @Query("SELECT COUNT(*) FROM laptops")
    fun getCount(): Int

    @Query("SELECT * FROM laptops WHERE id = :id")
    suspend fun getLaptopById(id: Int): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaptops(laptops: List<ItemEntity>)

    @Query("DELETE FROM laptops")
    suspend fun borrarTodo()
}
