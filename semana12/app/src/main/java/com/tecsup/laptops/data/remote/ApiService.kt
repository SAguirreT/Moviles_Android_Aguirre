package com.tecsup.laptops.data.remote

import com.tecsup.laptops.data.remote.dto.ItemDto
import com.tecsup.laptops.data.remote.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products/category/laptops")
    suspend fun getLaptops(): ProductResponseDto

    @GET("products/{id}")
    suspend fun getLaptopById(@Path("id") id: Int): ItemDto
}