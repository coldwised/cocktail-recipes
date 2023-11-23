package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

sealed interface CocktailsEvent {
    data object LoadCocktails: CocktailsEvent
    data object DismissChildSlot: CocktailsEvent

    data class OnCocktailClicked(val cocktail: Cocktail): CocktailsEvent
}
