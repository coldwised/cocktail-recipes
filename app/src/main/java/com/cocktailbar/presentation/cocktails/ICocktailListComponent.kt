package com.cocktailbar.presentation.cocktails

import kotlinx.coroutines.flow.StateFlow

interface ICocktailListComponent {
    val state: StateFlow<CocktailListState>

    fun dispatch(cocktailsUiEvent: CocktailsUiEvent)
}
