package com.cocktailbar.data.local

import android.net.Uri
import com.cocktailbar.data.local.entity.CocktailEntity
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow

interface CocktailDataSource {
    companion object {
        const val COCKTAIL_IMAGES_DIR = "images"
    }

    suspend fun getCocktails(): List<CocktailEntity>
    fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>>
    suspend fun deleteCocktailImage(path: String)
    suspend fun deleteCocktail(cocktailId: Long)
    suspend fun saveCocktail(cocktail: CocktailEntity)
}