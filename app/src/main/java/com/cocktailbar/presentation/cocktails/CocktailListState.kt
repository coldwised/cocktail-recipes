package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

data class CocktailListState(
    val loading: Boolean = true,
    val cocktails: List<Cocktail> = emptyList(),
)
