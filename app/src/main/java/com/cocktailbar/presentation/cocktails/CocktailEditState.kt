package com.cocktailbar.presentation.cocktails

data class CocktailEditState(
    val image: String? = null,
    val cachedExistingCocktailImage: String? = null,
    val imageLoaderProgressPercentage: Int? = null,
    val title: String = "",
    val description: String = "",
    val recipe: String = "",
    val removePictureLoading: Boolean = false,
    val ingredients: List<String> = emptyList(),
    val saveLoading: Boolean = false,
)
