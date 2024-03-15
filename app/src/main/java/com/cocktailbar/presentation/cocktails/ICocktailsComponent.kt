package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.cocktailbar.domain.model.Cocktail
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ICocktailsComponent {
    val childSlot: StateFlow<ChildSlot<*, SlotChild>>
    val cocktailListComponent: ICocktailListComponent
    val cocktailDetailImageComponent: ICocktailImageComponent
    val fabComponent: IFabComponent

    sealed interface SlotChild {
        data class CocktailDetailsChild(val component: ICocktailDetailsComponent) : SlotChild
    }

    fun navigateToCreateCocktail()

    fun saveAndDismissCocktail(cocktail: Cocktail)
}
