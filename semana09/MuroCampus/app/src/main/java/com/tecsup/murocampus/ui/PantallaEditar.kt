package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditar(
    viewModel: PostsViewModel,
    postId: Int,
    onBack: () -> Unit
) {
    val detalleState by viewModel.detalleState.collectAsStateWithLifecycle()
    val editarState by viewModel.editarState.collectAsStateWithLifecycle()

    val tituloOriginal = if (detalleState is UiState.Exito) (detalleState as UiState.Exito).data.title else ""
    var nuevoTitulo by remember(tituloOriginal) { mutableStateOf(tituloOriginal) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Título (PATCH)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Nota: JSONPlaceholder simula la modificación mediante PATCH y no la persiste permanentemente.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                label = { Text("Nuevo Título de la Publicación") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (editarState) {
                is UiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally))
                }
                is UiState.Error -> {
                    Text(
                        text = (editarState as UiState.Error).mensaje,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = { viewModel.editarPost(postId, nuevoTitulo) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Reintentar Guardar")
                    }
                }
                is UiState.Exito -> {
                    Text(
                        text = "¡Título modificado con éxito (Simulado)!",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.resetEditarState()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
                null -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.editarPost(postId, nuevoTitulo) },
                            enabled = nuevoTitulo.isNotBlank() && nuevoTitulo != tituloOriginal,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar cambios")
                        }
                    }
                }
            }
        }
    }
}
