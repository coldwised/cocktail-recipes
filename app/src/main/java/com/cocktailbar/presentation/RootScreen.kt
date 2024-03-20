package com.cocktailbar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.cocktailbar.presentation.cocktails.CocktailEditRootScreen
import com.cocktailbar.presentation.cocktails.CocktailsScreen

@Composable
fun RootScreen(root: IRootComponent) {
    val childStack by root.childStack.subscribeAsState()

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