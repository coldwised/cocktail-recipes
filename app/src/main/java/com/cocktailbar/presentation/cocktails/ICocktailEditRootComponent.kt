package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow

interface ICocktailEditRootComponent {
    val childSlot: StateFlow<ChildSlot<*, SlotChild>>
    sealed interface SlotChild {
        data class CocktailIngredient(val component: ICocktailIngredientComponent): SlotChild
    }
}
