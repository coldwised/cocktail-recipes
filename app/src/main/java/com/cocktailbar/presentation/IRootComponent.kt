package com.cocktailbar.presentation

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.stack.ChildStack
import com.cocktailbar.presentation.cocktails.ICocktailEditRootComponent
import com.cocktailbar.presentation.cocktails.ICocktailsComponent
import kotlinx.coroutines.flow.StateFlow

@Stable
interface IRootComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class CocktailsChild(val component: ICocktailsComponent) : Child
        data class CocktailRootEditChild(val component: ICocktailEditRootComponent) : Child
    }
}
