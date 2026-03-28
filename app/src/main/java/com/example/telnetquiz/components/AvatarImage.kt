package com.example.telnetquiz.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.telnetquiz.constants.AvatarConstants

@Composable
fun AvatarImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    localAvatarResId: Int? = null,
    gender: Boolean? = null,
    nameSeed: String = "",
    shape: Shape? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val clipModifier = if (shape != null) modifier.clip(shape) else modifier

    when {
        imageUrl != null -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "avatar",
                contentScale = contentScale,
                modifier = clipModifier
            )
        }
        localAvatarResId != null -> {
            Image(
                painter = painterResource(id = localAvatarResId),
                contentDescription = "avatar",
                contentScale = contentScale,
                modifier = clipModifier
            )
        }
        else -> {
            Image(
                painter = painterResource(
                    id = AvatarConstants.getDefaultAvatarResId(gender, nameSeed)
                ),
                contentDescription = "avatar",
                contentScale = contentScale,
                modifier = clipModifier
            )
        }
    }
}
