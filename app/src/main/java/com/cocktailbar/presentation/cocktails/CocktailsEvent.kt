package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

sealed interface CocktailsEvent {
    data object LoadCocktails : CocktailsEvent
    data object OnDismissCocktailDetails : CocktailsEvent
    data class OnCocktailClicked(val cocktail: Cocktail, val clickedCocktailImage: Any) : CocktailsEvent
}
