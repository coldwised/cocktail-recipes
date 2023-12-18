package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.util.RoundedButtonShape

@Composable
fun CocktailFab(fabComponent: IFabComponent) {
    val state = fabComponent.state.collectAsStateWithLifecycle().value
    val maxFabSize = remember { 80.dp }
    val fabSize by animateDpAsState(
        targetValue = if (state.visible) {
            maxFabSize
        } else 0.dp,
        label = "FAB"
    )
    FloatingActionButton(
        modifier = androidx.compose.ui.Modifier
            .size(fabSize),
        shape = RoundedButtonShape,
        onClick = fabComponent::onClick
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}