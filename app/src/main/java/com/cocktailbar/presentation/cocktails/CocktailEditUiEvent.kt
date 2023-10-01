package com.cocktailbar.presentation.cocktails

import android.net.Uri

sealed interface CocktailEditUiEvent

data object SaveCocktail: CocktailEditUiEvent
data class ChangeTitleValue(val value: String): CocktailEditUiEvent
data class ChangeDescriptionValue(val value: String): CocktailEditUiEvent
data class ChangeRecipeValue(val value: String): CocktailEditUiEvent
data class OnPickerResult(val uri: Uri?): CocktailEditUiEvent
data object OnCocktailPictureLoaderCompleted: CocktailEditUiEvent
