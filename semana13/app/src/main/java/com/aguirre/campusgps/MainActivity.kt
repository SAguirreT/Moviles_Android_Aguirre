
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

/**
 * MEJORA IA 1:
 * Actividad principal de CampusGPS.
 *
 * Se mantiene la arquitectura MVVM:
 * Activity -> ViewModel -> Repository.
 *
 * Se aplica el tema oscuro personalizado.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear los repositorios y la fabrica del ViewModel.
        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext),
            LugaresRepositoryLocal()
        )

        // Dibujar la interfaz con Jetpack Compose.
        setContent {

            CampusGPSTema {

                // Obtener el ViewModel mediante la fabrica.
                val vm: MapaViewModel = viewModel(
                    factory = factory
                )

                // Mostrar la pantalla principal.
                PantallaMapa(vm)
            }
        }
    }
}
