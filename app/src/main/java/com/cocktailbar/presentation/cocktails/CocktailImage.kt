package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cocktailbar.R

@Composable
fun CocktailImage(cocktailImageComponent: ICocktailImageComponent) {
    val state = cocktailImageComponent.state.collectAsStateWithLifecycle().value
    val image = state.image
    AnimatedVisibility(
        visible = state.visible,
        enter = slideInVertically(
            tween(300)
        ) + fadeIn(tween(300)),
        exit = slideOutVertically(
            tween(300)
        ) + fadeOut(tween(300))
    ) {
        AsyncImage(
            model = image ?: R.drawable.cocktail_placeholder,
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false, onClick = {}),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}