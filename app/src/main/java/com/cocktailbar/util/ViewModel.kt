package com.cocktailbar.util

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class ViewModel(mainContext: CoroutineContext, savedStateHandle: SavedStateHandle): InstanceKeeper.Instance {

    // The scope survives Android configuration changes
    private val viewModelScope = CoroutineScope(mainContext + SupervisorJob())

    override fun onDestroy() {
        viewModelScope.cancel()
    }
}