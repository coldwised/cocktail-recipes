package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

data class CocktailListState(
    val isLoading: Boolean = true,
    val cocktails: List<Cocktail> = emptyList(),
)
