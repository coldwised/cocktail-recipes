package com.cocktailbar.domain.use_case

import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.domain.model.Cocktail
import me.tatarka.inject.annotations.Inject

@Inject
class SaveCocktailUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(cocktail: Cocktail) {
        repository.saveCocktail(cocktail)
    }
}