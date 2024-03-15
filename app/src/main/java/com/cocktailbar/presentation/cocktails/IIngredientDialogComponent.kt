package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface IIngredientDialogComponent {
    val state: StateFlow<IngredientDialogState>

    fun dispatch(ingredientDialogEvent: IngredientDialogEvent)
}
