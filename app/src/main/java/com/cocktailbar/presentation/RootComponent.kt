package com.cocktailbar.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.presentation.cocktails.CocktailEditRootComponent
import com.cocktailbar.presentation.cocktails.CocktailsComponent
import com.cocktailbar.presentation.cocktails.CocktailsEvent
import com.cocktailbar.util.toStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RootComponent(
    @Assisted componentContext: ComponentContext,
    private val cocktailsFactory:
        (ComponentContext, (Cocktail?) -> Unit) -> CocktailsComponent,
    private val cocktailEditRootComponentFactory:
        (
        ComponentContext,
        Cocktail?,
        () -> Unit,
        () -> Unit
    ) -> CocktailEditRootComponent
) : IRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, IRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = ChildConfig.Cocktails,
            handleBackButton = true,
            childFactory = ::createChild,
        ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): IRootComponent.Child {
        return when (config) {
            is ChildConfig.Cocktails -> {
                IRootComponent.Child.CocktailsChild(
                    cocktailsFactory(
                        componentContext,
                        { cocktail -> navigation.push(ChildConfig.CocktailEdit(cocktail)) }
                    )
                )
            }

            is ChildConfig.CocktailEdit -> {
                IRootComponent.Child.CocktailRootEditChild(
                    cocktailEditRootComponentFactory(
                        componentContext,
                        config.cocktail,
                        {
                            navigation.pop {
                                val cocktailsComponent = (childStack.value.active.instance as IRootComponent.Child.CocktailsChild).component
                                cocktailsComponent.dismissCocktailDetails()
                                cocktailsComponent.cocktailListComponent.dispatch(CocktailsEvent.LoadCocktails)
                            }
                        },
                        { navigation.pop() }
                    )
                )
            }
        }
    }

    @Parcelize
    private sealed interface ChildConfig : Parcelable {
        @Parcelize
        data object Cocktails : ChildConfig

        @Parcelize
        data class CocktailEdit(val cocktail: Cocktail?) : ChildConfig
    }
}