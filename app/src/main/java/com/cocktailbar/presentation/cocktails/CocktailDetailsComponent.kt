package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.DeleteCocktailUseCase
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
class CocktailDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val cocktail: Cocktail,
    @Assisted private val navigateToEditCocktail: (Cocktail) -> Unit,
    @Assisted private val navigateBack: () -> Unit,
    @Assisted private val navigateBackWithRefresh: () -> Unit,
    private val deleteCocktailUseCase: DeleteCocktailUseCase,
) : ICocktailDetailsComponent, ComponentContext by componentContext {
    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _deletingInProgress = MutableStateFlow(false)
    override val deletingInProgress = _deletingInProgress.asStateFlow()
    override fun onEditClick() {
        navigateToEditCocktail(cocktail)
    }

    override fun onDeleteClick() {
        componentScope.launch {
            _deletingInProgress.update { true }
            deleteCocktailUseCase(cocktail)
            _deletingInProgress.update { false }
            navigateBackWithRefresh()
        }
    }

    private val backCallback = BackCallback {
        navigateBack()
    }

    init {
        backHandler.register(backCallback)
    }
}