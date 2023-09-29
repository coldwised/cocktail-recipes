package com.cocktailbar.util

sealed interface DownloadState<T> {
    data class Downloading<T>(val progress: Int) : DownloadState<T>
    data class Finished<T>(val value: T) : DownloadState<T>
}