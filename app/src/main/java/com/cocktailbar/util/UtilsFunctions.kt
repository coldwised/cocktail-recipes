package com.cocktailbar.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }


@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }

fun <T : Any> Value<T>.toStateFlow(lifecycle: Lifecycle): StateFlow<T> {
    val state = MutableStateFlow(this.value)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        val observer = { value: T -> state.value = value }
        subscribe(observer)
        lifecycle.doOnDestroy {
            unsubscribe(observer)
        }
    }

    return state
}

val RoundedButtonShape = RoundedCornerShape(80.dp)

fun CoroutineScope(context: CoroutineContext, lifecycle: Lifecycle): CoroutineScope {
    val scope = CoroutineScope(context)
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}

fun getImageCacheKey(uri: String): String {
    return uri.split('/').last().substringBefore('_')
}

fun LifecycleOwner.coroutineScope(context: CoroutineContext): CoroutineScope =
    CoroutineScope(context, lifecycle)