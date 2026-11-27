package com.tecsup.murocampus.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.theme.BorderSoft
import com.tecsup.murocampus.ui.theme.CelestePastel
import com.tecsup.murocampus.ui.theme.DeleteRed
import com.tecsup.murocampus.ui.theme.ErrorRedBg
import com.tecsup.murocampus.ui.theme.TextPrimary
import com.tecsup.murocampus.ui.theme.TextSecondary
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    viewModel: PostsViewModel,
    postId: Int,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit
) {
    val detalleState by viewModel.detalleState.collectAsStateWithLifecycle()
    val comentariosState by viewModel.comentariosState.collectAsStateWithLifecycle()
    val eliminarState by viewModel.eliminarState.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    // Diálogo de confirmación de eliminación con DELETE
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ErrorRedBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Advertencia", tint = DeleteRed, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(text = "¿Eliminar publicación?", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                }
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar esta publicación? Nota: JSONPlaceholder simula la eliminación y no la conserva permanentemente.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.eliminarPost(postId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeleteRed),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, BorderSoft)
                ) {
                    Text("Cancelar", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                }
            }
        )
    }

    if (eliminarState is UiState.Exito) {
        viewModel.resetEliminarState()
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de publicación", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
            when (eliminarState) {
                is UiState.Cargando -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                else -> {}
            }

            when (detalleState) {
                is UiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is UiState.Error -> {
                    Text(text = (detalleState as UiState.Error).mensaje, color = DeleteRed)
                    Button(
                        onClick = { viewModel.seleccionarPost(postId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Reintentar")
                    }
                }
                is UiState.Exito -> {
                    val post = (detalleState as UiState.Exito).data
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(CelestePastel),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Usuario ${post.userId}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Publicación académica",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 16.sp,
                                color = TextSecondary,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(color = BorderSoft, thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Botones profesionales Editar y Eliminar
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { onEdit(postId) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Editar", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRedBg),
                                    shape = RoundedCornerShape(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = DeleteRed, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Eliminar", color = DeleteRed, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            val totalComentarios = if (comentariosState is UiState.Exito) (comentariosState as UiState.Exito).data.size else 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comentarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CelestePastel)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "$totalComentarios",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            when (comentariosState) {
                is UiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is UiState.Error -> {
                    Text(text = (comentariosState as UiState.Error).mensaje, color = DeleteRed)
                }
                is UiState.Exito -> {
                    val comentarios = (comentariosState as UiState.Exito).data
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(comentarios, key = { it.id }) { com ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, BorderSoft),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(CelestePastel),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Avatar Comentario",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = com.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = com.email,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = com.body,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextSecondary,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
