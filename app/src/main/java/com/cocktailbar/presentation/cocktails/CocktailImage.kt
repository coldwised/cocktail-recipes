package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cocktailbar.R

@Composable
fun CocktailImage(cocktailImageComponent: ICocktailImageComponent) {
    val state = cocktailImageComponent.state.collectAsStateWithLifecycle().value
    val image = state.image
    val placeHolderId = remember { R.drawable.cocktail_placeholder }
    AnimatedVisibility(
        visible = state.visible,
        enter = slideInVertically(
            tween(300)
        ) + fadeIn(tween(300)),
        exit = slideOutVertically(
            tween(300)
        ) + fadeOut(tween(300))
    ) {
        val contentScale = ContentScale.Crop
        val imageCacheKey = image?.split('/')?.last()?.substringBefore('_')
        val coilPainter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(image ?: placeHolderId)
                .placeholder(placeHolderId)
                .memoryCacheKey(imageCacheKey)
                .placeholderMemoryCacheKey(imageCacheKey)
                .build(),
            contentScale = contentScale
        )
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false, onClick = {}),
            painter = coilPainter,
            contentDescription = null,
            contentScale = contentScale
        )
    }
}