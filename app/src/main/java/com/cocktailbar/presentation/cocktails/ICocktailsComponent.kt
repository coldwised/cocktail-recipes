package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow

interface ICocktailsComponent {
    val childSlot: StateFlow<ChildSlot<*, SlotChild>>
    val cocktailListComponent: ICocktailListComponent
    val cocktailDetailImageComponent: ICocktailImageComponent
    val fabComponent: IFabComponent

    sealed interface SlotChild {
        data class CocktailDetailsChild(val component: ICocktailDetailsComponent) : SlotChild
    }

    fun dismissCocktailDetails()

    fun navigateToCreateCocktail()
    fun dismissCocktailDetailsWithUpdate()
}
