package com.cocktailbar.data.local

import cocktaildb.CocktailEntity

interface CocktailDataSource {
    suspend fun getCocktails(): List<CocktailEntity>

    suspend fun addCocktail(
        id: Long?,
        name: String,
        description: String,
        recipe: String,
        ingredients: String,
    )
}