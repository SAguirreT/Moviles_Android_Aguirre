package com.tecsup.laptops.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.laptops.data.repository.ItemRepository
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                itemRepository.getFavoritos().collect { listaFavoritos ->
                    _uiState.value = UiState.Exito(listaFavoritos)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar los favoritos")
            }
        }
    }

    fun toggleFavorito(item: Item) {
        viewModelScope.launch {
            itemRepository.updateFavorito(item.id, !item.esFavorito)
        }
    }

    companion object {
        fun provideFactory(itemRepository: ItemRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FavoritosViewModel(itemRepository)
            }
        }
    }
}
