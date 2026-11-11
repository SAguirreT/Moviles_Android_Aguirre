package com.tecsup.laptops.data.repository

import com.tecsup.laptops.domain.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoritosManager {
    private val _favoritos = MutableStateFlow<Map<Int, Item>>(emptyMap())
    val favoritos: StateFlow<Map<Int, Item>> = _favoritos.asStateFlow()

    fun toggleFavorito(item: Item) {
        val current = _favoritos.value.toMutableMap()
        if (current.containsKey(item.id)) {
            current.remove(item.id)
        } else {
            current[item.id] = item.copy(esFavorito = true)
        }
        _favoritos.value = current
    }

    fun isFavorito(id: Int): Boolean {
        return _favoritos.value.containsKey(id)
    }
}
