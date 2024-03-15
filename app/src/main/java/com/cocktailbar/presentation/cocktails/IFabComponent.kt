package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface IFabComponent {
    val state: StateFlow<FabState>
    fun onClick()

    fun changeVisibility(visible: Boolean)
}
