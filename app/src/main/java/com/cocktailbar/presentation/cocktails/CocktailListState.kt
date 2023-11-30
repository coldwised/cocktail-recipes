package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.util.UiText

data class CocktailListState(
    val isLoading: Boolean = true,
    val cocktails: List<Cocktail> = emptyList(),
    val error: UiText? = null,
    val clickedCocktailImage: Any? = null,
)
