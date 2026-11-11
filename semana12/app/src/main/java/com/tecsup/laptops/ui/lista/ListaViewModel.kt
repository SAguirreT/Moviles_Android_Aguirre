package com.tecsup.laptops.ui.lista

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ListaViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        observarLaptops()
        cargarLaptops()
    }

    private fun observarLaptops() {
        viewModelScope.launch {
            itemRepository.getLaptops()
                .catch { e ->
                    _uiState.value = UiState.Error("Error al leer base de datos local: ${e.localizedMessage}")
                }
                .collect { items ->
                    if (items.isNotEmpty()) {
                        _uiState.value = UiState.Exito(items)
                    }
                }
        }
    }

    fun cargarLaptops() {
        viewModelScope.launch {
            if (_uiState.value !is UiState.Exito) {
                _uiState.value = UiState.Cargando
            }
            val result = itemRepository.refreshLaptops()
            result.onFailure { e ->
                if (_uiState.value !is UiState.Exito) {
                    _uiState.value = UiState.Error("Error al cargar laptops: ${e.localizedMessage}")
                }
            }
        }
    }

    fun toggleFavorito(item: Item) {
        viewModelScope.launch {
            itemRepository.updateFavorito(item.id, !item.esFavorito)
        }
    }

    fun refrescar() {
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

    fun limpiarAviso() {
        _aviso.value = null
    }

    companion object {
        fun provideFactory(itemRepository: ItemRepository, notificador: Notificador): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListaViewModel(itemRepository, notificador)
            }
        }
    }
}
