package com.cocktailbar

import com.cocktailbar.data.local.entity.CocktailEntity
import com.cocktailbar.domain.model.Cocktail

fun CocktailEntity.toCocktail(): Cocktail {
    return Cocktail(
        id = id,
        name = name,
        description = description,
        recipe = recipe,
        ingredients = ingredients?.let { if(it.isBlank()) emptyList() else it.split("ъы") },
        image = image,
    )
}

fun Cocktail.toCocktailEntity(): CocktailEntity {
    return CocktailEntity(
        id = id,
        name = name,
        description = description,
        recipe = recipe,
        ingredients = ingredients?.joinToString("ъы"),
        image = image
    )
}