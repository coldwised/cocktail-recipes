package com.cocktailbar.data.local

import android.net.Uri
import cocktaildb.CocktailEntity
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CocktailDataSource {
    suspend fun getCocktails(): List<CocktailEntity>

    suspend fun addCocktail(
        id: Long?,
        name: String,
        description: String,
        recipe: String,
        ingredients: String,
    )

    fun getCocktailImagesDir(): File

    companion object {
        const val NAME = "cocktail.db"
        const val COCKTAIL_IMAGES_DIR = "images"
    }

    fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>>
}