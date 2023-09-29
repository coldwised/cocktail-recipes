package com.cocktailbar.data.local

import android.content.Context
import java.io.File

class FileProviderImpl(
    private val context: Context,
): FileProvider {
    override fun getFile(fileName: String): File {
        return context.getDir(fileName, Context.MODE_PRIVATE)
    }
}