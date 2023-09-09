package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.domain.model.Cocktail
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted val cocktail: Cocktail,
    @Assisted private val navigateToEditCocktail: (Cocktail) -> Unit
): ICocktailDetailsComponent, ComponentContext by componentContext {

    fun onEditClick() {
        navigateToEditCocktail(cocktail)
    }
}