package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ICocktailEditComponent {
    val state: StateFlow<CocktailEditState>
    fun dispatch(cocktailsUiEvent: CocktailEditUiEvent)
}
