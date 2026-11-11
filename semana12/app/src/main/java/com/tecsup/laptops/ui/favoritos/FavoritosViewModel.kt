package com.tecsup.laptops.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.data.repository.FavoritosManager
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
                FavoritosManager.favoritos.collectLatest { favMap ->
                    _uiState.value = UiState.Exito(favMap.values.toList())
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar los favoritos")
            }
        }
    }
}
