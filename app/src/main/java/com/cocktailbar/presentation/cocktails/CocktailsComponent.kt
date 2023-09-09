package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.GetCocktailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val navigateToCocktail: (Cocktail) -> Unit,
    private val getCocktailsUseCase: GetCocktailsUseCase,
): ICocktailsComponent, ComponentContext by componentContext {

    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _state = MutableStateFlow(CocktailsState())
    val state = _state.asStateFlow()

    init {
        dispatch(CocktailsUiEvent.LoadCocktails)
    }

    fun dispatch(cocktailsUiEvent: CocktailsUiEvent) {
        reduce(cocktailsUiEvent, state.value)
    }

    private fun reduce(event: CocktailsUiEvent, state: CocktailsState) {
        componentScope.launch {
            when(event) {
                is CocktailsUiEvent.LoadCocktails -> {
                    setStateLoading()
                    getCocktailsUseCase().collect { cocktails ->
                        updateState(state.copy(cocktails = cocktails, isLoading = false))
                    }
                }
                is CocktailsUiEvent.OnCocktailClicked -> {
                    navigateToCocktail(event.cocktail)
                }
            }
        }
    }

    private fun setStateLoading() {
        updateState(CocktailsState())
    }

    private fun updateState(newState: CocktailsState) {
        _state.value = newState
    }
}