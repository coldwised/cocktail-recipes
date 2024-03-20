package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

@Stable
interface ICocktailEditRootComponent {
    val childSlot: Value<ChildSlot<*, SlotChild>>
    val cocktailEditComponent: ICocktailEditComponent

    sealed interface SlotChild {
        data class CocktailIngredient(val component: IIngredientDialogComponent) : SlotChild
    }
}
