package com.tecsup.laptops.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val uiState: StateFlow<UiState<Item>> = _uiState.asStateFlow()

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                val laptop = Item(
                    id = id,
                    titulo = "MacBook Pro 16",
                    descripcion = "Laptop de alto rendimiento con chip M3 Max, 32GB RAM y SSD de 1TB.",
                    precio = 2499.99,
                    marca = "Apple",
                    imagenUrl = null
                )
                _uiState.value = UiState.Exito(laptop)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al obtener el detalle de la laptop")
            }
        }
    }
}