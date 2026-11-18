package com.aguirre.campusgps.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aguirre.campusgps.data.LugaresRepository
import com.aguirre.campusgps.data.UbicacionRepository
class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MapaViewModel::class.java))
        return MapaViewModel(ubicacionRepo, lugaresRepo) as T
    }
}
