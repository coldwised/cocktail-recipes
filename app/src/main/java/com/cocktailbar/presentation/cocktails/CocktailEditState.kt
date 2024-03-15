package com.cocktailbar.presentation.cocktails

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
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
) : Parcelable

