package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.di.MainImmediateDispatcher
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.DeleteCocktailUseCase
import com.cocktailbar.domain.use_case.GetCocktailsUseCase
import com.cocktailbar.domain.use_case.SaveCocktailUseCase
import com.cocktailbar.util.coroutineScope
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
    private val deleteCocktailUseCase: DeleteCocktailUseCase,
    private val saveCocktailUseCase: SaveCocktailUseCase,
    mainImmediateDispatcher: MainImmediateDispatcher
) : ComponentContext by componentContext, ICocktailListComponent {
    private val componentScope = coroutineScope(mainImmediateDispatcher + SupervisorJob())

    private val _state = MutableStateFlow(CocktailListState())
    override val state = _state.asStateFlow()

    private val emptyState = CocktailListState()

    init {
        dispatch(CocktailsEvent.LoadCocktails)
    }

    override fun dispatch(cocktailsEvent: CocktailsEvent) {
        reduce(cocktailsEvent)
    }

    private fun reduce(event: CocktailsEvent) {
        componentScope.launch {
            val stateFlow = _state
            when (event) {
                is CocktailsEvent.LoadCocktails -> {
                    stateFlow.update { emptyState }
                    getCocktailsUseCase().collect { cocktails ->
                        stateFlow.update {
                            it.copy(cocktails = cocktails, loading = false)
                        }
                    }
                }

                is CocktailsEvent.OnCocktailClicked -> {
                    navigateToCocktailDetails(event.cocktail)
                }

                is CocktailsEvent.OnAddCocktail -> {
                    stateFlow.update { it.copy(loading = true) }
                    saveCocktailUseCase(event.cocktail)
                    dispatch(CocktailsEvent.LoadCocktails)
                }

                is CocktailsEvent.OnDeleteCocktail -> {
                    val cocktailToDelete = event.cocktail
                    deleteCocktailUseCase(cocktailToDelete)
                    stateFlow.update {
                        val cocktailId = cocktailToDelete.id
                        it.copy(
                            cocktails = it.cocktails.filter { cocktail ->
                                cocktail.id != cocktailId
                            }
                        )
                    }
                }
            }
        }
    }
}