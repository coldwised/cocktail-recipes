package com.cocktailbar.presentation.cocktails

import kotlinx.coroutines.flow.StateFlow

interface IIngredientDialogComponent {
    val state: StateFlow<IngredientDialogState>

    fun dispatch(ingredientDialogEvent: IngredientDialogEvent)
}
