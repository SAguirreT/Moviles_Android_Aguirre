package com.tecsup.laptops.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("products") val products: List<ItemDto>
)

data class ItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("brand") val brand: String?,
    @SerializedName("thumbnail") val thumbnail: String?
)