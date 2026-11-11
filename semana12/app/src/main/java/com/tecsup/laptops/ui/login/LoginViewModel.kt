package com.tecsup.laptops.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.laptops.ui.UiState
import com.tecsup.laptops.utils.toUserFriendlyMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _uiState = MutableStateFlow<UiState<Unit>?>(null)
    val uiState: StateFlow<UiState<Unit>?> = _uiState.asStateFlow()

    fun login(email: String, clave: String, onExito: () -> Unit) {
        if (email.isBlank() || clave.isBlank()) {
            _uiState.value = UiState.Error("Por favor, completa todos los campos.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _uiState.value = UiState.Error("Ingresa un correo electrónico válido.")
            return
        }

        if (clave.length < 6) {
            _uiState.value = UiState.Error("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                auth.signInWithEmailAndPassword(email.trim(), clave.trim()).await()
                _uiState.value = UiState.Exito(Unit)
                onExito()
            } catch (e: Throwable) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                val errorMsg = e.message ?: ""
                val mensaje = when {
                    errorMsg.contains("badly formatted", true) -> "Formato de correo electrónico inválido."
                    errorMsg.contains("invalid-credential", true) || errorMsg.contains("user-not-found", true) || errorMsg.contains("wrong-password", true) -> "Correo o contraseña incorrectos."
                    errorMsg.contains("network", true) || errorMsg.contains("timeout", true) || errorMsg.contains("unreachable", true) -> "No hay conexión a internet."
                    else -> e.toUserFriendlyMessage()
                }
                _uiState.value = UiState.Error(mensaje)
            }
        }
    }
}
