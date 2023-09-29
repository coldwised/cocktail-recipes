package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.GetCocktailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val emptyState = CocktailListState()

    init {
        dispatch(CocktailsUiEvent.LoadCocktails)
    }

    override fun dispatch(cocktailsUiEvent: CocktailsUiEvent) {
        reduce(cocktailsUiEvent)
    }

    private fun reduce(event: CocktailsUiEvent) {
        componentScope.launch {
            val stateFlow = _state
            when(event) {
                is CocktailsUiEvent.LoadCocktails -> {
                    stateFlow.update { emptyState }
                    getCocktailsUseCase().collect { cocktails ->
                        stateFlow.update {
                            it.copy(cocktails = cocktails, isLoading = false)
                        }
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
}