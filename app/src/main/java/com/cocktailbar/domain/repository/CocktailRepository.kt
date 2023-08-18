package com.cocktailbar.domain.repository

import com.cocktailbar.presentation.Cocktail
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getCocktailList(): Flow<List<Cocktail>>

    fun addCocktail(cocktail: Cocktail)
}