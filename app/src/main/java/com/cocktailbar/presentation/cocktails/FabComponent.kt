package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FabComponent(
    componentContext: ComponentContext,
    private val navigateToCreateCocktail: () -> Unit,
): IFabComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(
        stateKeeper.consume(key = "FAB_STATE") ?: FabState()
    )

    override val state: StateFlow<FabState> = _state.asStateFlow()

    init {
        stateKeeper.register("FAB_STATE") { _state.value }
    }

    override fun onClick() = navigateToCreateCocktail()

    override fun changeVisibility(visible: Boolean) {
        _state.update { it.copy(visible = visible) }
    }
}