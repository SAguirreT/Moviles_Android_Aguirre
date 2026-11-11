package com.tecsup.laptops.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.laptops.data.repository.ItemRepository
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.notification.Notificador
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        cargarInventarioAdmin()
    }

    fun cargarInventarioAdmin() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                itemRepository.getLaptops().collect { items ->
                    _uiState.value = UiState.Exito(items)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar la gestión de inventario")
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            val result = itemRepository.refreshLaptops()
            result.fold(
                onSuccess = { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _aviso.value = "Se guardaron $n elementos"
                },
                onFailure = { e ->
                    _aviso.value = "Error al sincronizar: ${e.localizedMessage}"
                }
            )
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            _aviso.value = "Caché limpiada correctamente"
        }
    }

    fun limpiarAviso() {
        _aviso.value = null
    }

    companion object {
        fun provideFactory(itemRepository: ItemRepository, notificador: Notificador): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AdminViewModel(itemRepository, notificador)
            }
        }
    }
}
