package com.cocktailbar.domain.use_case

import com.cocktailbar.domain.repository.CocktailRepository
import com.cocktailbar.presentation.Cocktail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCocktailsUseCase @Inject constructor(
    private val repository: CocktailRepository
) {

    operator fun invoke(): Flow<List<Cocktail>> {
        return repository.getCocktailList()
    }
}