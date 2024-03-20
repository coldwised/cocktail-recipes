package com.cocktailbar.presentation.cocktails

import kotlinx.serialization.Serializable

@Serializable
data class FabState(
    val visible: Boolean = true
)
