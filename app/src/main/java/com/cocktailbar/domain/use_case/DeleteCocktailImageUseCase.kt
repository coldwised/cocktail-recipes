package com.cocktailbar.domain.use_case

import com.cocktailbar.domain.repository.CocktailRepository
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteCocktailImageUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(path: String) {
        repository.deleteCocktailImage(path)
    }
}