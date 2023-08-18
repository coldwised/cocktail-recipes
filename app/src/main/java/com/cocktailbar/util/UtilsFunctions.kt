package com.cocktailbar.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import io.github.xxfast.decompose.LocalComponentContext

@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    key: Any = T::class,
    crossinline block: @DisallowComposableCalls () -> T
): T {
    val component: ComponentContext = LocalComponentContext.current
    val packageName: String? = T::class.qualifiedName
    val viewModelKey = "$packageName.view-model"
    return remember(key) {
        component.instanceKeeper.getOrCreate(viewModelKey) { block() }
    }
}