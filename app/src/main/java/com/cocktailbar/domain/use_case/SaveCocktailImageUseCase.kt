package com.cocktailbar.domain.use_case

import android.net.Uri
import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.util.DownloadState
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SaveCocktailImageUseCase(
    private val repository: CocktailRepository
) {
    operator fun invoke(uri: Uri): Flow<DownloadState<String>> {
        return repository.saveCocktailImage(uri)
    }
}