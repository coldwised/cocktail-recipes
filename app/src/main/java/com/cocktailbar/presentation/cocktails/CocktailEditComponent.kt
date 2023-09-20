package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.AddCocktailUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailEditComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted cocktail: Cocktail?,
    private val addCocktailUseCase: AddCocktailUseCase,
): ComponentContext by componentContext, ICocktailEditComponent {
    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val _state = MutableStateFlow(CocktailEditState())

    override val state = _state.asStateFlow()

    override fun dispatch(cocktailsUiEvent: CocktailEditUiEvent) {
        reduce(cocktailsUiEvent, state.value)
    }

    private fun reduce(event: CocktailEditUiEvent, state: CocktailEditState) {
        componentScope.launch {
            when(event) {
                is SaveCocktail -> {
                    updateState(CocktailEditState(saveLoading = true))
                    addCocktailUseCase(Cocktail(
                        id = null,
                        name = state.title,
                        description = state.description,
                        recipe = state.recipe,
                        ingredients = state.ingredients
                    ))
                    updateState(CocktailEditState(saveLoading = false))
                }
                is ChangeTitleValue -> {
                    updateState(state.copy(title = event.value))
                }
                is ChangeDescriptionValue -> {
                    updateState(state.copy(description = event.value))
                }
                is ChangeRecipeValue -> {
                    updateState(state.copy(recipe = event.value))
                }
            }
        }
    }

    private fun updateState(newState: CocktailEditState) {
        _state.value = newState
    }
}