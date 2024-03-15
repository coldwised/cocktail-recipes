package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import com.cocktailbar.domain.model.Cocktail

@Stable
interface ICocktailDetailsComponent {
    val cocktail: Cocktail

    fun onEditClick()

    fun onDeleteClick()
}
