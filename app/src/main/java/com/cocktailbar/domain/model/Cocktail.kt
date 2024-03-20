package com.cocktailbar.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Cocktail(
    val id: Long? = null,
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: List<String>,
    val image: String?,
)
