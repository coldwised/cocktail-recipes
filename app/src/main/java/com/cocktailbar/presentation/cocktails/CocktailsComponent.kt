package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
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
    private val cocktailDetailsFactory: (
        ComponentContext,
        Cocktail,
        (Cocktail) -> Unit,
    ) -> CocktailDetailsComponent,
    private val cocktailListFactory: (
        ComponentContext,
        (Cocktail) -> Unit,
    ) -> CocktailListComponent,
) : ICocktailsComponent, ComponentContext by componentContext {
    private val slotNavigation = SlotNavigation<SlotConfig>()
    private val navigation = StackNavigation<ChildConfig>()

    @Parcelize
    private sealed interface SlotConfig : Parcelable {
        @Parcelize
        data class CocktailDetails(val cocktail: Cocktail) : SlotConfig
    }

    @Parcelize
    private sealed interface ChildConfig : Parcelable {
        @Parcelize
        data object CocktailList : ChildConfig
    }

    override val childStack: StateFlow<ChildStack<*, ICocktailsComponent.Child>> =
        childStack(
            source = navigation,
            handleBackButton = true,
            initialConfiguration = ChildConfig.CocktailList,
            childFactory = ::createChild
        ).toStateFlow(lifecycle)


    override val childSlot: StateFlow<ChildSlot<*, ICocktailsComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            handleBackButton = true,
            childFactory = ::createSlotChild
        ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): ICocktailsComponent.Child {
        return when(config) {
            is ChildConfig.CocktailList -> {
                ICocktailsComponent.Child.CocktailList(
                    cocktailListFactory(
                        componentContext,
                        { cocktail -> slotNavigation.activate(SlotConfig.CocktailDetails(cocktail)) }
                    )
                )
            }
        }
    }


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
                        {}
                    )
                )
            }
        }
    }
}