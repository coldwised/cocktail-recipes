package com.cocktailbar.presentation.cocktails

import kotlinx.coroutines.flow.StateFlow

interface IFabComponent {
    val state: StateFlow<FabState>
    fun onClick()

    fun changeVisibility(visible: Boolean)
}
