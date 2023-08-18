package com.cocktailbar.domain.use_case

import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.presentation.Cocktail
import javax.inject.Inject

class AddCocktailUseCase @Inject constructor(
    private val repository: CocktailRepository
) {

    operator fun invoke(cocktail: Cocktail) {
        repository.addCocktail(cocktail)
    }
}