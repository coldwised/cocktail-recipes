package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.essenty.parcelable.Parcelable
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.util.toStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

class CocktailEditRootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext, ICocktailEditRootComponent {
    private val slotNavigation = SlotNavigation<SlotConfig>()

    @Parcelize
    private sealed interface SlotConfig : Parcelable {
        @Parcelize
        data class CocktailIngredient(val cocktail: Cocktail) : SlotConfig
    }

    override val childSlot: StateFlow<ChildSlot<*, ICocktailEditRootComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            handleBackButton = true,
            childFactory = ::createSlotChild
        ).toStateFlow(lifecycle)


    private fun createSlotChild(
        config: SlotConfig,
        componentContext: ComponentContext
    ): ICocktailEditRootComponent.SlotChild {
        return when(config) {
            is SlotConfig.CocktailIngredient -> {
                ICocktailEditRootComponent.SlotChild.CocktailIngredient(
                    CocktailIngredientComponent(componentContext)
                )
            }
        }
    }
}