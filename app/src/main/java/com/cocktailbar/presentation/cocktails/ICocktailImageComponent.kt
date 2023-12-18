package com.cocktailbar.presentation.cocktails

import kotlinx.coroutines.flow.StateFlow

interface ICocktailImageComponent {

    val state: StateFlow<CocktailImageState>
    fun show(image: String?)
    fun hide()
}
