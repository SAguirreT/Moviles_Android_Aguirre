package com.aguirre.campusgps
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aguirre.campusgps.data.LugaresRepositoryLocal
import com.aguirre.campusgps.data.UbicacionRepositoryGoogle
import com.aguirre.campusgps.ui.PantallaMapa
import com.aguirre.campusgps.ui.theme.CampusGPSTema
import com.aguirre.campusgps.viewmodel.CampusViewModelFactory
import com.aguirre.campusgps.viewmodel.MapaViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = CampusViewModelFactory(UbicacionRepositoryGoogle(applicationContext), LugaresRepositoryLocal())
        setContent { CampusGPSTema {
            val vm: MapaViewModel = viewModel(factory = factory)
            PantallaMapa(vm)
        } }
    }
}
