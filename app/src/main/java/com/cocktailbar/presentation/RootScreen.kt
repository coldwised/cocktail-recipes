package com.cocktailbar.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.cocktailbar.presentation.cocktails.CocktailsScreen
import com.cocktailbar.presentation.cocktails.EditCocktailScreen

@Composable
fun RootScreen(root: IRootComponent) {
    val childStack = root.childStack.collectAsStateWithLifecycle().value

    Children(stack = childStack) {
        when(val child = it.instance) {
            is IRootComponent.Child.CocktailsChild -> {
                CocktailsScreen(child.component)
            }
            is IRootComponent.Child.CocktailEditChild -> {
                EditCocktailScreen(child.component)
            }
        }
    }
}