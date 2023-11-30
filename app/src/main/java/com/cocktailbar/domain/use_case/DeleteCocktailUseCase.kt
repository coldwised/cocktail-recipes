package com.cocktailbar.domain.use_case

import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.repository.CocktailRepository
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteCocktailUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(cocktail: Cocktail) {
        repository.deleteCocktail(cocktail)
    }
}