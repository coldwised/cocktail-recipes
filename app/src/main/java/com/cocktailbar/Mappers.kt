package com.cocktailbar

import com.cocktailbar.data.local.CocktailEntity
import com.cocktailbar.presentation.Cocktail

fun CocktailEntity.toCocktail(): Cocktail {
    return Cocktail(
        name = name,
        description = description,
        recipe = recipe,
        ingredients = ingredients.split(',')
    )
}

fun Cocktail.toCocktailEntity(): CocktailEntity {
    return CocktailEntity(
        name = name,
        description = description,
        recipe = recipe,
        ingredients = ingredients.joinToString(",")
    )
}