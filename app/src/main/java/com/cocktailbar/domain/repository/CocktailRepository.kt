package com.cocktailbar.domain.repository

import com.cocktailbar.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getCocktailList(): Flow<List<Cocktail>>

    suspend fun addCocktail(cocktail: Cocktail)
}