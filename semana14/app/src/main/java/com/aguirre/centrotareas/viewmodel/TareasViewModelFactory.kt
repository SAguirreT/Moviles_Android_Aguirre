package com.aguirre.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aguirre.centrotareas.data.TareasRepositoryLocal

class TareasViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(TareasViewModel::class.java))
        return TareasViewModel(TareasRepositoryLocal()) as T
    }
}
