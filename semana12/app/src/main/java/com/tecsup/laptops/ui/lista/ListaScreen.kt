package com.tecsup.laptops.ui.lista

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecsup.laptops.domain.model.Item

@Composable
fun ListaContent(
    items: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(items) { item ->
            ItemCard(
                item = item,
                onItemClick = onItemClick
            )
        }
    }
}