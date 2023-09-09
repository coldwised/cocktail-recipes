package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.util.UiText

data class CocktailsState(
    val isLoading: Boolean = true,
    val cocktails: List<Cocktail> = emptyList(),
    val error: UiText? = null,
)
