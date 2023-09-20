package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface ICocktailsComponent {
    val childSlot: StateFlow<ChildSlot<*, SlotChild>>
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface SlotChild {
        data class CocktailDetailsChild(val component: ICocktailDetailsComponent): SlotChild
    }

    sealed interface Child {
        data class CocktailList(val component: ICocktailListComponent): Child
    }

    fun navigateToCreateCocktail()
}
