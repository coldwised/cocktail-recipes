package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

sealed interface CocktailsEvent {
    data object LoadCocktails : CocktailsEvent
    data class OnCocktailClicked(val cocktail: Cocktail) : CocktailsEvent
    data class OnAddCocktail(val cocktail: Cocktail): CocktailsEvent
    data class OnDeleteCocktail(val cocktail: Cocktail): CocktailsEvent
}
