package com.cocktailbar.domain.model

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    val id: Long? = null,
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: List<String>,
    val image: String?,
): Parcelable
