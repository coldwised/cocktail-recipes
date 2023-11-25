package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.parcelable.Parcelable
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.util.toStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailEditRootComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted cocktail: Cocktail?,
    @Assisted navigateToCocktailsWithRefresh: () -> Unit,
    @Assisted navigateBack: () -> Unit,
    cocktailEditComponentFactory: (
        ComponentContext,
        Cocktail?,
        () -> Unit,
        () -> Unit,
        () -> Unit
    ) -> CocktailEditComponent
) : ComponentContext by componentContext, ICocktailEditRootComponent {
    private val slotNavigation = SlotNavigation<SlotConfig>()
    override val cocktailEditComponent: ICocktailEditComponent = cocktailEditComponentFactory(
        childContext("ICocktailEditComponent"),
        cocktail,
        { slotNavigation.activate(SlotConfig.CocktailIngredient) },
        { navigateToCocktailsWithRefresh() },
        { navigateBack() }
    )

    @Parcelize
    private sealed interface SlotConfig : Parcelable {
        @Parcelize
        data object CocktailIngredient : SlotConfig
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
        return when (config) {
            is SlotConfig.CocktailIngredient -> {
                ICocktailEditRootComponent.SlotChild.CocktailIngredient(
                    IngredientDialogComponent(
                        componentContext = componentContext,
                        dismissDialog = slotNavigation::dismiss,
                        saveIngredient = { cocktailEditComponent.dispatch(SaveIngredientValue(it)) }
                    )
                )
            }
        }
    }
}