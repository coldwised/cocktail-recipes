package com.cocktailbar.data.local

import android.content.ContentResolver
import android.net.Uri
import cocktaildb.CocktailEntity
import com.cocktailbar.CocktailDatabase
import com.cocktailbar.data.local.CocktailDataSource.Companion.COCKTAIL_IMAGES_DIR
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class CocktailDataSourceImpl(
    db: CocktailDatabase,
    fileProvider: FileProvider,
    private val contentResolver: ContentResolver,
) : CocktailDataSource {

    private val queries = db.cocktailQueries

    private val cocktailImagesDir = fileProvider.getFile(COCKTAIL_IMAGES_DIR)

    override suspend fun getCocktails(): List<CocktailEntity> {
        return queries.getCocktails().executeAsList()
    }

    private fun generateUniqueFileName(uri: Uri): String {
        val code = "${uri.toString().split('/').last()}_${System.currentTimeMillis()}"
        return "$code.jpg"
    }

    override fun saveCocktailImage(uri: Uri): Flow<DownloadState<String>> {
        return flow {
            emit(DownloadState.Downloading(0))
            val imageFile = File(cocktailImagesDir, generateUniqueFileName(uri))
            imageFile.outputStream().use { outputStream ->
                contentResolver.openAssetFileDescriptor(uri, "r").use { fileDescriptor ->
                    fileDescriptor?.createInputStream()?.use { inputStream ->
                        val totalBytes = fileDescriptor.length
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = inputStream.read(buffer)
                        while(bytes > 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(DownloadState.Downloading((progressBytes * 100 / totalBytes).toInt()))
                        }
                    }
                }
            }
            emit(DownloadState.Finished(imageFile.path))
        }
    }

    override suspend fun deleteCocktailImage(path: String) {
        File(path).let {
            if(it.exists()) it.delete()
        }
    }

    override fun getCocktailImagesDir() = cocktailImagesDir

    override suspend fun deleteCocktail(cocktailId: Long) {
        queries.deleteCocktail(cocktailId)
    }


    override suspend fun addCocktail(
        id: Long?,
        name: String,
        description: String,
        recipe: String,
        ingredients: String,
        image: String?
    ) {
        queries.addCocktail(
            id = id,
            name = name,
            description = description,
            recipe = recipe,
            ingredients = ingredients,
            image = image
        )
    }
}