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
class CocktailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val navigateToEditCocktailScreen: (Cocktail?) -> Unit,
    private val cocktailDetailsFactory: (
        ComponentContext,
        Cocktail,
        (Cocktail) -> Unit,
        () -> Unit,
        () -> Unit
    ) -> CocktailDetailsComponent,
    cocktailListFactory: (
        ComponentContext,
        (Cocktail) -> Unit
    ) -> CocktailListComponent,
) : ICocktailsComponent, ComponentContext by componentContext {
    private val slotNavigation = SlotNavigation<SlotConfig>()

    private fun navigateToCocktailDetails(cocktail: Cocktail) {
        slotNavigation.activate(SlotConfig.CocktailDetails(cocktail)) {
            cocktailDetailImageComponent.show(cocktail.image)
            fabComponent.changeVisibility(false)
        }
    }

    override val cocktailListComponent: ICocktailListComponent = cocktailListFactory(
        childContext(key = "cocktailListComponent"),
        ::navigateToCocktailDetails
    )

    override val cocktailDetailImageComponent: ICocktailImageComponent = CocktailImageComponent(
        childContext("cocktailImageComponent")
    )

    override val fabComponent: IFabComponent = FabComponent(
        componentContext = childContext("fabComponent"),
        navigateToCreateCocktail = ::navigateToCreateCocktail
    )

    override fun dismissCocktailDetails() {
        slotNavigation.dismiss {
            cocktailDetailImageComponent.hide()
            fabComponent.changeVisibility(true)
        }
    }

    override fun dismissCocktailDetailsWithUpdate() {
        slotNavigation.dismiss {
            cocktailDetailImageComponent.hide()
            fabComponent.changeVisibility(true)
            cocktailListComponent.dispatch(CocktailsEvent.LoadCocktails)
        }
    }

    @Parcelize
    private sealed interface SlotConfig : Parcelable {
        @Parcelize
        data class CocktailDetails(val cocktail: Cocktail) : SlotConfig
    }

    override val childSlot: StateFlow<ChildSlot<*, ICocktailsComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            handleBackButton = true,
            childFactory = ::createSlotChild
        ).toStateFlow(lifecycle)

    private fun createSlotChild(
        slotConfig: SlotConfig,
        componentContext: ComponentContext,
    ): ICocktailsComponent.SlotChild {
        return when (slotConfig) {
            is SlotConfig.CocktailDetails -> {
                ICocktailsComponent.SlotChild.CocktailDetailsChild(
                    cocktailDetailsFactory(
                        componentContext,
                        slotConfig.cocktail,
                        navigateToEditCocktailScreen,
                        ::dismissCocktailDetails,
                        ::dismissCocktailDetailsWithUpdate
                    )
                )
            }
        }
    }

    override fun navigateToCreateCocktail() = navigateToEditCocktailScreen(null)
}