package com.cocktailbar.data.local

import cocktaildb.CocktailEntity
import com.cocktailbar.CocktailDatabase

class CocktailDataSourceImpl(db: CocktailDatabase): CocktailDataSource {

    private val queries = db.cocktailQueries

    override suspend fun getCocktails(): List<CocktailEntity> {
        return queries.getCocktails().executeAsList()
    }

    override suspend fun addCocktail(
        id: Long?,
        name: String,
        description: String,
        recipe: String,
        ingredients: String,
    ) {
        queries.addCocktail(
            id = id,
            name = name,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
    }
}