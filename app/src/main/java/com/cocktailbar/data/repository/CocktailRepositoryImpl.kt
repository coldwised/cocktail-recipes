package com.cocktailbar.data.repository

import com.cocktailbar.data.local.CocktailDao
import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.presentation.Cocktail
import com.cocktailbar.toCocktail
import com.cocktailbar.toCocktailEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val cocktailDao: CocktailDao,
): CocktailRepository {
    override fun getCocktailList(): Flow<List<Cocktail>> {
        return flow {
            emit(cocktailDao.getCocktails().map { it.toCocktail() })
        }
    }

    override fun addCocktail(cocktail: Cocktail) {
        cocktailDao.insertCocktail(cocktail.toCocktailEntity())
    }
}