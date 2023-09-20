package com.cocktailbar.presentation.cocktails

sealed interface CocktailEditUiEvent

data object SaveCocktail: CocktailEditUiEvent

data class ChangeTitleValue(val value: String): CocktailEditUiEvent
data class ChangeDescriptionValue(val value: String): CocktailEditUiEvent
data class ChangeRecipeValue(val value: String): CocktailEditUiEvent
