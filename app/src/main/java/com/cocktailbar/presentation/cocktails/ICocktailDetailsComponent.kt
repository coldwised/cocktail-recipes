package com.cocktailbar.presentation.cocktails

import com.cocktailbar.domain.model.Cocktail
import kotlinx.coroutines.flow.StateFlow

interface ICocktailDetailsComponent {
    val cocktail: Cocktail

    fun onEditClick()

    fun onDeleteClick()
    val isDeleteInProcess: StateFlow<Boolean>
}
