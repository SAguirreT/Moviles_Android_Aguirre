package com.aguirre.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aguirre.centrotareas.ui.PantallaTareas
import com.aguirre.centrotareas.ui.theme.CentroTareasTheme
import com.aguirre.centrotareas.viewmodel.TareasViewModel
import com.aguirre.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory())
                PantallaTareas(vm)
            }
        }
    }
}
