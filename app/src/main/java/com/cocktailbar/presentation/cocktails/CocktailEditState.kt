package com.cocktailbar.presentation.cocktails

data class CocktailEditState(
    val title: String = "",
    val description: String = "",
    val recipe: String = "",
    val ingredients: List<String> = emptyList(),
    val saveLoading: Boolean = false,
)
