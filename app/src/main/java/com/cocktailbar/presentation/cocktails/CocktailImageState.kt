package com.cocktailbar.presentation.cocktails

import android.os.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class CocktailImageState(
    val image: String? = null,
    val visible: Boolean = false
) : Parcelable
