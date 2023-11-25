package com.cocktailbar.presentation.cocktails

sealed interface IngredientDialogEvent {
    data class OnIngredientTextChanged(val value: String) : IngredientDialogEvent
    data object OnDismiss : IngredientDialogEvent
    data object OnSaveIngredient : IngredientDialogEvent
}
