package com.cocktailbar.domain.use_case

import com.cocktailbar.di.Singleton
import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.domain.model.Cocktail
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class AddCocktailUseCase(
    private val repository: CocktailRepository
) {
    suspend operator fun invoke(cocktail: Cocktail) {
        repository.addCocktail(cocktail)
    }
}