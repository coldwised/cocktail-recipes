package com.cocktailbar.presentation.cocktails

import android.os.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class FabState(
    val visible: Boolean = true
) : Parcelable
