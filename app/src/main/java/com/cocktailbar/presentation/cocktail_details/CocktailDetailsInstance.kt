package com.cocktailbar.presentation.cocktail_details

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.cocktailbar.presentation.Cocktail
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CocktailDetailsInstance(savedState: SavedStateHandle, cocktail: Cocktail): InstanceKeeper.Instance {
    private val initialState: CocktailDetailsState = savedState.get() ?: CocktailDetailsState(cocktail)
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    fun abc() {
        scop
    }

    override fun onDestroy() {
        return
    }
}