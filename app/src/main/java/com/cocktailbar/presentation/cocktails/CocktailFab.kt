package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.util.RoundedButtonShape

@Composable
fun CocktailFab(fabComponent: IFabComponent) {
    val state by fabComponent.state.collectAsStateWithLifecycle()
    val fabScale by animateFloatAsState(
        targetValue = if (state.visible) {
            1f
        } else 0f,
        label = "FAB"
    )
    FloatingActionButton(
        modifier = Modifier
            .size(80.dp)
            .graphicsLayer {
                scaleX = fabScale
                scaleY = fabScale
            },
        shape = RoundedButtonShape,
        onClick = fabComponent::onClick
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}