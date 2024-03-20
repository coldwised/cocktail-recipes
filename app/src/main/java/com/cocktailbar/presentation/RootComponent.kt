package com.cocktailbar.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.presentation.cocktails.CocktailEditRootComponent
import com.cocktailbar.presentation.cocktails.CocktailsComponent
import kotlinx.serialization.Serializable
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
        (Cocktail) -> Unit,
        () -> Unit
    ) -> CocktailEditRootComponent
) : IRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: Value<ChildStack<*, IRootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = ChildConfig.Cocktails,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = ChildConfig.serializer()
        )

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
                        { cocktail ->
                            navigation.pop {
                                val cocktailsComponent =
                                    (childStack.value.active.instance as IRootComponent.Child.CocktailsChild).component
                                cocktailsComponent.saveAndDismissCocktail(cocktail)
                            }
                        },
                        { navigation.pop() }
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface ChildConfig {
        @Serializable
        data object Cocktails : ChildConfig

        @Serializable
        data class CocktailEdit(val cocktail: Cocktail?) : ChildConfig
    }
}