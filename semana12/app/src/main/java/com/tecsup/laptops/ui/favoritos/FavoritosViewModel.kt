package com.tecsup.laptops.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                // Lista simulada de favoritos
                val listaFavoritos = listOf(
                    Item(1, "MacBook Pro 16", "Laptop de alto rendimiento con M3 Max", 2499.99, "Apple", null, esFavorito = true)
                )
                _uiState.value = UiState.Exito(listaFavoritos)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar los favoritos")
            }
        }
    }
}