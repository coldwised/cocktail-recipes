package com.cocktailbar.data.local

import java.io.File

interface FileProvider {
    fun getFile(fileName: String): File
}