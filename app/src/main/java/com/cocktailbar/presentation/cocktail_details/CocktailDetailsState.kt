package com.cocktailbar.presentation.cocktail_details

import com.arkivanov.essenty.parcelable.Parcelable
import com.cocktailbar.presentation.Cocktail
import kotlinx.parcelize.Parcelize

@Parcelize
data class CocktailDetailsState(
    val cocktail: Cocktail
): Parcelable