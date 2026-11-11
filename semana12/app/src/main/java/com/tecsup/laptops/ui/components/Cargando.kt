package com.tecsup.laptops.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecsup.laptops.ui.theme.Dimens

@Composable
fun Cargando(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.M),
        verticalArrangement = Arrangement.spacedBy(Dimens.M)
    ) {
        ShimmerItemCardSkeleton()
        ShimmerItemCardSkeleton()
    }
}
