package com.cocktailbar.domain.repository

import android.net.Uri
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getCocktailList(): Flow<List<Cocktail>>

    suspend fun addCocktail(cocktail: Cocktail)
    fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>>
    suspend fun deleteCocktailImage(path: String)
    suspend fun deleteCocktail(cocktail: Cocktail)
}