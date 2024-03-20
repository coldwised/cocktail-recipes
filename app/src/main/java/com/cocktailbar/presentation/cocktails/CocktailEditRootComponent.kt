package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.cocktailbar.domain.model.Cocktail
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailEditRootComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted cocktail: Cocktail?,
    @Assisted saveAndDismissCocktail: (Cocktail) -> Unit,
    @Assisted navigateBack: () -> Unit,
    cocktailEditComponentFactory: (
        ComponentContext,
        Cocktail?,
        () -> Unit,
        (Cocktail) -> Unit,
        () -> Unit
    ) -> CocktailEditComponent
) : ComponentContext by componentContext, ICocktailEditRootComponent {
    private val slotNavigation = SlotNavigation<SlotConfig>()
    override val cocktailEditComponent: ICocktailEditComponent = cocktailEditComponentFactory(
        childContext("cocktailEditComponent"),
        cocktail,
        { slotNavigation.activate(SlotConfig.CocktailIngredient) },
        { cocktail -> saveAndDismissCocktail(cocktail) },
        { navigateBack() }
    )

    private val backCallback = BackCallback {
        cocktailEditComponent.dispatch(OnCancelClick)
    }

    init {
        backHandler.register(backCallback)
    }

    @Serializable
    private sealed interface SlotConfig {
        @Serializable
        data object CocktailIngredient : SlotConfig
    }

    override val childSlot: Value<ChildSlot<*, ICocktailEditRootComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            handleBackButton = true,
            serializer = SlotConfig.serializer(),
            childFactory = ::createSlotChild
        )


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