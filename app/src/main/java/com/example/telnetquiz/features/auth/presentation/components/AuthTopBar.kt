package com.example.telnetquiz.features.auth.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.components.TopBarContainer

@Composable
fun AuthTopBar(
    @DrawableRes imageId: Int,
    contentDescription: String? = null,
    contentAlignment: Alignment = Alignment.TopStart
) {
    TopBarContainer(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageId)
                .build(),
            contentDescription = contentDescription
        )
    }
}
