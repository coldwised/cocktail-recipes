package com.cocktailbar.presentation.cocktails

data class CocktailEditState(
    val image: String? = null,
    val imageLoaderProgressPercentage: Int? = null,
    val title: String = "",
    val description: String = "",
    val recipe: String = "",
    val ingredients: List<String> = emptyList(),
    val saveLoading: Boolean = false,
)
