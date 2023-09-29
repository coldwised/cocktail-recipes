package com.cocktailbar.presentation.cocktails

data class CocktailEditState(
    val image: String? = null,
    val imageLoaderProgress: Int = LOADER_PROGRESS_COMPLETED,
    val title: String = "",
    val description: String = "",
    val recipe: String = "",
    val ingredients: List<String> = emptyList(),
    val saveLoading: Boolean = false,
) {
    companion object {
        const val LOADER_PROGRESS_COMPLETED = 100
    }
}
