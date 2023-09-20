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
class CocktailListComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val navigateToCocktailDetails: (Cocktail) -> Unit,
    private val getCocktailsUseCase: GetCocktailsUseCase,
): ComponentContext by componentContext, ICocktailListComponent {
    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _state = MutableStateFlow(CocktailListState())
    override val state = _state.asStateFlow()

    init {
        dispatch(CocktailsUiEvent.LoadCocktails)
    }

    override fun dispatch(cocktailsUiEvent: CocktailsUiEvent) {
        reduce(cocktailsUiEvent, state.value)
    }

    private fun reduce(event: CocktailsUiEvent, state: CocktailListState) {
        componentScope.launch {
            when(event) {
                is CocktailsUiEvent.LoadCocktails -> {
                    setStateLoading()
                    getCocktailsUseCase().collect { cocktails ->
                        updateState(state.copy(cocktails = cocktails, isLoading = false))
                    }
                }
                is CocktailsUiEvent.OnCocktailClicked -> {
                    navigateToCocktailDetails(event.cocktail)
                }
                is CocktailsUiEvent.DismissChildSlot -> {
                }
            }
        }
    }

    private fun setStateLoading() {
        updateState(CocktailListState())
    }

    private fun updateState(newState: CocktailListState) {
        _state.value = newState
    }
}