package com.cocktailbar.data.repository

import android.net.Uri
import com.cocktailbar.data.local.CocktailDataSource
import com.cocktailbar.di.Singleton
import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.toCocktail
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class CocktailRepositoryImpl(
    private val cocktailDataSource: CocktailDataSource,
): CocktailRepository {
    override fun getCocktailList(): Flow<List<Cocktail>> {
        return flow {
            emit(cocktailDataSource.getCocktails().map { it.toCocktail() })
        }.flowOn(Dispatchers.IO)
    }

    override fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>> {
        return cocktailDataSource.saveCocktailImage(uri).flowOn(Dispatchers.IO)
    }

    override suspend fun deleteCocktailImage(path: String) {
        cocktailDataSource.deleteCocktailImage(path)
    }

    override suspend fun addCocktail(cocktail: Cocktail) {
        cocktailDataSource.addCocktail(
            id = cocktail.id,
            name = cocktail.name,
            description = cocktail.description,
            recipe = cocktail.recipe,
            ingredients = cocktail.ingredients.joinToString(",")
        )
    }
}