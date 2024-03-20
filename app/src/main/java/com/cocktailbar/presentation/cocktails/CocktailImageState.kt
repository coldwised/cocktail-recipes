package com.cocktailbar.presentation.cocktails

import kotlinx.serialization.Serializable


@Serializable
data class CocktailImageState(
    val image: String? = null,
    val visible: Boolean = false
)
