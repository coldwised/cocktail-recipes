package com.cocktailbar

import cocktaildb.CocktailEntity
import com.cocktailbar.domain.model.Cocktail

fun CocktailEntity.toCocktail(): Cocktail {
    return Cocktail(
        id = id,
        name = name,
        description = description,
        recipe = recipe,
        ingredients = ingredients.let { if(it.isBlank()) emptyList() else it.split(',') },
        image = image,
    )
}