package com.tecsup.laptops.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.laptops.data.mapper.toDomain
import com.tecsup.laptops.data.remote.RetrofitClient
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.notification.Notificador
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        cargarLaptops()
    }

    fun cargarLaptops() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                val response = RetrofitClient.apiService.getLaptops()
                val items = response.products.map { it.toDomain() }
                _uiState.value = UiState.Exito(items)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar la lista de laptops: ${e.localizedMessage}")
            }
        }
    }

    fun refrescar() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getLaptops()
                val items = response.products.map { it.toDomain() }
                _uiState.value = UiState.Exito(items)
                val n = items.size
                notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                _aviso.value = "Se guardaron $n elementos"
            } catch (e: Exception) {
                _aviso.value = "Error al refrescar: ${e.localizedMessage}"
            }
        }
    }

    fun limpiarAviso() {
        _aviso.value = null
    }

    companion object {
        fun provideFactory(notificador: Notificador): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListaViewModel(notificador)
            }
        }
    }
}
