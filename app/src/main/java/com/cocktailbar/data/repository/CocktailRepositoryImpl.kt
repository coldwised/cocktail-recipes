package com.cocktailbar.data.repository

import android.net.Uri
import com.cocktailbar.data.local.CocktailDataSource
import com.cocktailbar.di.AppCoroutineScope
import com.cocktailbar.di.IoDispatcher
import com.cocktailbar.di.Singleton
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.toCocktail
import com.cocktailbar.toCocktailEntity
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class CocktailRepositoryImpl(
    private val cocktailDataSource: CocktailDataSource,
    private val appCoroutineScope: AppCoroutineScope,
    private val ioDispatcher: IoDispatcher
) : CocktailRepository {
    override fun getCocktailList(): Flow<List<Cocktail>> {
        return flow {
            emit(cocktailDataSource.getCocktails().map { it.toCocktail() })
        }.flowOn(ioDispatcher)
    }

    override fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>> {
        return cocktailDataSource.saveCocktailImage(uri)
    }

    override suspend fun deleteCocktail(cocktail: Cocktail) {
        appCoroutineScope.launch(ioDispatcher) {
            val cocktailDataSource = cocktailDataSource
            cocktail.image?.let {
                cocktailDataSource.deleteCocktailImage(it)
            }
            cocktail.id?.let {
                cocktailDataSource.deleteCocktail(it)
            }
        }
    }

    override suspend fun deleteCocktailImage(path: String) {
        appCoroutineScope.launch(ioDispatcher) {
            cocktailDataSource.deleteCocktailImage(path)
        }
    }

    override suspend fun saveCocktail(cocktail: Cocktail) {
        cocktailDataSource.saveCocktail(
            cocktail.toCocktailEntity()
        )
    }
}