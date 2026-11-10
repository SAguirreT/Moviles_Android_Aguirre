package com.tecsup.laptops.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.laptops.data.auth.AuthRepository
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Exito(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun login(email: String, clave: String, onExito: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            val res = authRepository.iniciarSesion(email, clave)
            if (res.isSuccess) {
                _uiState.value = UiState.Exito(Unit)
                onExito()
            } else {
                _uiState.value = UiState.Error(res.exceptionOrNull()?.message ?: "Error al iniciar sesión")
            }
        }
    }
}