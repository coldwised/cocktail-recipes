package com.cocktailbar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.cocktailbar.presentation.cocktails.CocktailEditRootScreen
import com.cocktailbar.presentation.cocktails.CocktailsScreen

@Composable
fun RootScreen(root: IRootComponent) {
    val childStack by root.childStack.collectAsStateWithLifecycle()

    Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is IRootComponent.Child.CocktailsChild -> {
                CocktailsScreen(child.component)
            }

            is IRootComponent.Child.CocktailRootEditChild -> {
                CocktailEditRootScreen(child.component)
            }
        }
    }
}