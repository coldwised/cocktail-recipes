package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CocktailImageComponent(
    componentContext: ComponentContext
) : ICocktailImageComponent, ComponentContext by componentContext {
    private val _state = MutableStateFlow(
        stateKeeper.consume(key = "COCKTAIL_IMAGE_STATE", strategy = CocktailImageState.serializer()) ?: CocktailImageState()
    )

    override val state = _state.asStateFlow()

    init {
        stateKeeper.register("COCKTAIL_IMAGE_STATE", strategy = CocktailImageState.serializer()) { _state.value }
    }

    override fun show(image: String?) {
        _state.update { it.copy(image = image, visible = true) }
    }

    override fun hide() {
        _state.update { it.copy(visible = false) }
    }
}