package com.tecsup.laptops.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.laptops.data.mapper.toDomain
import com.tecsup.laptops.data.repository.ItemRepository
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.ui.UiState
import com.tecsup.laptops.utils.toUserFriendlyMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val uiState: StateFlow<UiState<Item>> = _uiState.asStateFlow()

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                // Primero busca en Room (Offline-First / datos guardados sin internet)
                val itemLocal = itemRepository.getLaptopById(id)
                if (itemLocal != null) {
                    _uiState.value = UiState.Exito(itemLocal)
                } else {
                    val dto = com.tecsup.laptops.data.remote.RetrofitClient.apiService.getLaptopById(id)
                    val laptop = dto.toDomain()
                    _uiState.value = UiState.Exito(laptop)
                }
            } catch (e: Throwable) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                val itemLocal = itemRepository.getLaptopById(id)
                if (itemLocal != null) {
                    _uiState.value = UiState.Exito(itemLocal)
                } else {
                    _uiState.value = UiState.Error(e.toUserFriendlyMessage())
                }
            }
        }
    }

    fun toggleFavorito(item: Item) {
        viewModelScope.launch {
            val nuevoEstado = !item.esFavorito
            itemRepository.updateFavorito(item.id, nuevoEstado)
            if (_uiState.value is UiState.Exito) {
                _uiState.value = UiState.Exito((_uiState.value as UiState.Exito<Item>).datos.copy(esFavorito = nuevoEstado))
            }
        }
    }

    companion object {
        fun provideFactory(itemRepository: ItemRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetalleViewModel(itemRepository)
            }
        }
    }
}
