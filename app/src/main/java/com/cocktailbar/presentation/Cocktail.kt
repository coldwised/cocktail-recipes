package com.cocktailbar.presentation

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: List<String>
): Parcelable
