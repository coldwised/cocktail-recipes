package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail

interface ICocktailDetailsComponent {
    val cocktail: Cocktail

    fun onEditClick()
}
