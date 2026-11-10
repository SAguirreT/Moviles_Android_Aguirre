package com.tecsup.laptops.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarInventarioAdmin()
    }

    fun cargarInventarioAdmin() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                val inventario = listOf(
                    Item(1, "MacBook Pro 16", "Laptop de alto rendimiento con M3 Max", 2499.99, "Apple", null),
                    Item(2, "Dell XPS 15", "Pantalla 4K OLED e Intel i9", 1999.99, "Dell", null),
                    Item(3, "Lenovo ThinkPad X1", "Diseño ultraligero y resistente para trabajo", 1499.99, "Lenovo", null)
                )
                _uiState.value = UiState.Exito(inventario)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar la gestión de inventario")
            }
        }
    }
}