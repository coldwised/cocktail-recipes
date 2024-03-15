package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ICocktailEditRootComponent {
    val childSlot: StateFlow<ChildSlot<*, SlotChild>>
    val cocktailEditComponent: ICocktailEditComponent

    sealed interface SlotChild {
        data class CocktailIngredient(val component: IIngredientDialogComponent) : SlotChild
    }
}
