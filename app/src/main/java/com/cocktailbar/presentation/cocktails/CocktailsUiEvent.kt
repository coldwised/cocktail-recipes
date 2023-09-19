package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

sealed interface CocktailsUiEvent {
    object LoadCocktails: CocktailsUiEvent
    object DismissChildSlot: CocktailsUiEvent

    data class OnCocktailClicked(val cocktail: Cocktail): CocktailsUiEvent
}
