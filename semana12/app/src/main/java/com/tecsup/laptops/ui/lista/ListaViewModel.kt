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
import com.tecsup.laptops.utils.toUserFriendlyMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        observarItems()
        refrescar()
    }

    private fun observarItems() {
        viewModelScope.launch {
            repo.items
                .catch { e ->
                    _uiState.value = UiState.Error(e.toUserFriendlyMessage())
                }
                .collect { lista ->
                    _uiState.value = UiState.Exito(lista)
                }
        }
    }

    fun refrescar() {
        viewModelScope.launch {
            if (repo.cantidadGuardada() == 0 && _uiState.value !is UiState.Exito) {
                _uiState.value = UiState.Cargando
            }
            
            val result = repo.refrescar()
            result.fold(
                onSuccess = { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _aviso.value = "Se guardaron $n elementos"
                },
                onFailure = { e ->
                    if (repo.cantidadGuardada() == 0) {
                        _uiState.value = UiState.Error(e.toUserFriendlyMessage())
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
            )
        }
    }

    fun cargarLaptops() {
        _uiState.value = UiState.Cargando
        refrescar()
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(id)
        }
    }

    fun toggleFavorito(item: Item) {
        alternarFavorito(item.id)
    }

    fun limpiarAviso() {
        _aviso.value = null
    }

    companion object {
        fun provideFactory(repo: ItemRepository, notificador: Notificador): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListaViewModel(repo, notificador)
            }
        }
    }
}
