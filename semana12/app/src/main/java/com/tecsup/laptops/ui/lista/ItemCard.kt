package com.tecsup.laptops.ui.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.laptops.domain.model.Item

@Composable
fun ItemCard(
    item: Item,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // URL de respaldo con imagenes reales de laptops de alta calidad en Unsplash
    val urlImagenFinal = when {
        !item.imagenUrl.isNullOrBlank() -> item.imagenUrl
        item.marca.contains("Apple", ignoreCase = true) -> "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500&q=80"
        item.marca.contains("Dell", ignoreCase = true) -> "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500&q=80"
        item.marca.contains("Lenovo", ignoreCase = true) -> "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=500&q=80"
        item.marca.contains("HP", ignoreCase = true) -> "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500&q=80"
        item.marca.contains("Asus", ignoreCase = true) -> "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=500&q=80"
        else -> "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500&q=80"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(72.dp)
            ) {
                AsyncImage(
                    model = urlImagenFinal,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                SuggestionChip(
                    onClick = { },
                    label = { Text(item.marca, fontSize = 12.sp) },
                    modifier = Modifier.height(26.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$ ${item.precio}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}