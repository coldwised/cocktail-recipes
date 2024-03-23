package com.cocktailbar.data.local

import android.content.ContentResolver
import android.net.Uri
import com.cocktailbar.data.local.CocktailDataSource.Companion.COCKTAIL_IMAGES_DIR
import com.cocktailbar.data.local.entity.CocktailEntity
import com.cocktailbar.di.IoDispatcher
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File

class CocktailDataSourceImpl(
    private val cocktailDao: CocktailDao,
    fileProvider: FileProvider,
    private val contentResolver: ContentResolver,
    private val ioDispatcher: IoDispatcher,
) : CocktailDataSource {

    private val cocktailImagesDir = fileProvider.getFile(COCKTAIL_IMAGES_DIR)

    override suspend fun getCocktails(): List<CocktailEntity> {
        return withContext(ioDispatcher) {
            cocktailDao.getCocktails()
        }
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
                        while (true) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            if (bytes <= 0) {
                                break
                            }
                            emit(DownloadState.Downloading((progressBytes * 100f / totalBytes).toInt()))
                        }
                    }
                }
            }
            emit(DownloadState.Finished(imageFile.path))
        }.flowOn(ioDispatcher)
    }

    override suspend fun deleteCocktailImage(path: String) {
        withContext(ioDispatcher) {
            File(path).let {
                if (it.exists()) it.delete()
            }
        }
    }

    override suspend fun deleteCocktail(cocktailId: Long) {
        withContext(ioDispatcher) {
            cocktailDao.deleteCocktail(cocktailId)
        }
    }


    override suspend fun saveCocktail(
        cocktail: CocktailEntity
    ) {
        withContext(ioDispatcher) {
            cocktailDao.saveCocktail(cocktail)
        }
    }
}