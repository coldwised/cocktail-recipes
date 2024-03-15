package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface ICocktailImageComponent {

    val state: StateFlow<CocktailImageState>
    fun show(image: String?)
    fun hide()
}
