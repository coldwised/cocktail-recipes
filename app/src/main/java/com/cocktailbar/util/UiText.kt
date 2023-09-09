package com.cocktailbar.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {

    fun asString(context: Context): String

    @Composable
    fun asString(): String


    class DynamicString(private val value: String): UiText {
        override fun asString(context: Context): String {
            return value
        }

        @Composable
        override fun asString(): String {
            return value
        }
    }

    class StringResource(@StringRes private val resId: Int, private vararg val args: String): UiText {
        override fun asString(context: Context): String {
            return context.getString(resId, args)
        }

        @Composable
        override fun asString(): String {
            return stringResource(resId)
        }
    }
}

