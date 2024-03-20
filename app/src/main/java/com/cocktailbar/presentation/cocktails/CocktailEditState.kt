package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class CocktailEditState(
    val image: String? = null,
    val cachedExistingCocktailImage: String? = null,
    val imageLoaderProgressPercentage: Int? = null,
    val title: String = "",
    val description: String = "",
    val recipe: String = "",
    val cancellationInProgress: Boolean = false,
    val ingredients: List<String> = emptyList(),
)

