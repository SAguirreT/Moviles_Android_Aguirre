package com.tecsup.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.theme.BorderSoft
import com.tecsup.murocampus.ui.theme.CelestePastel
import com.tecsup.murocampus.ui.theme.DeleteRed
import com.tecsup.murocampus.ui.theme.TextPrimary
import com.tecsup.murocampus.ui.theme.TextSecondary
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
                title = { Text("Editar publicación", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // Cabecera descriptiva
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CelestePastel),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Editar publicación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Actualiza la información de tu aviso",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, BorderSoft)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Text(
                        text = "Nota: JSONPlaceholder simula la modificación mediante PATCH y no la persiste permanentemente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    OutlinedTextField(
                        value = nuevoTitulo,
                        onValueChange = { nuevoTitulo = it },
                        label = { Text("Título de la publicación") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = BorderSoft,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (editarState) {
                is UiState.Cargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = (editarState as UiState.Error).mensaje,
                        color = DeleteRed,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(
                        onClick = { viewModel.editarPost(postId, nuevoTitulo) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Reintentar Guardar", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
                is UiState.Exito -> {
                    Text(
                        text = "¡Título modificado con éxito (Simulado)!",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(
                        onClick = {
                            viewModel.resetEditarState()
                            onBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Volver", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
                null -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, BorderSoft)
                        ) {
                            Text("Cancelar", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { viewModel.editarPost(postId, nuevoTitulo) },
                            enabled = nuevoTitulo.isNotBlank() && nuevoTitulo != tituloOriginal,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Guardar cambios", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
