package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ICocktailListComponent {
    val state: StateFlow<CocktailListState>

    fun dispatch(cocktailsEvent: CocktailsEvent)
}
