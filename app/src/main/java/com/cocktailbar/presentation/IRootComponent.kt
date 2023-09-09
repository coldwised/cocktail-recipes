package com.cocktailbar.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.cocktailbar.presentation.cocktails.ICocktailDetailsComponent
import com.cocktailbar.presentation.cocktails.ICocktailEditComponent
import com.cocktailbar.presentation.cocktails.ICocktailsComponent
import kotlinx.coroutines.flow.StateFlow

interface IRootComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class CocktailsChild(val component: ICocktailsComponent): Child
        data class CocktailDetailsChild(val component: ICocktailDetailsComponent): Child
        data class CocktailEditChild(val component: ICocktailEditComponent): Child
    }
}
