package com.cocktailbar.presentation.cocktails

import kotlinx.coroutines.flow.StateFlow

interface ICocktailEditComponent {
    val state: StateFlow<CocktailEditState>
    fun dispatch(cocktailsUiEvent: CocktailEditUiEvent)
}
