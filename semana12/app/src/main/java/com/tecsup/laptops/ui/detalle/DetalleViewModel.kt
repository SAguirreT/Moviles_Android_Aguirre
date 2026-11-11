package com.tecsup.laptops.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.data.mapper.toDomain
import com.tecsup.laptops.data.remote.RetrofitClient
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
                // Llamada real a Retrofit para obtener el detalle de la laptop por ID
                val dto = RetrofitClient.apiService.getLaptopById(id)
                val laptop = dto.toDomain()
                _uiState.value = UiState.Exito(laptop)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al obtener el detalle de la laptop: ${e.localizedMessage}")
            }
        }
    }
}
