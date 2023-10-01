package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.AddCocktailUseCase
import com.cocktailbar.domain.use_case.DeleteCocktailImageUseCase
import com.cocktailbar.domain.use_case.SaveCocktailImageUseCase
import com.cocktailbar.util.DownloadState
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
class CocktailEditComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted cocktail: Cocktail?,
    private val addCocktailUseCase: AddCocktailUseCase,
    private val saveCocktailImageUseCase: SaveCocktailImageUseCase,
    private val deleteCocktailImageUseCase: DeleteCocktailImageUseCase
) : ComponentContext by componentContext, ICocktailEditComponent {
    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val _state = MutableStateFlow(CocktailEditState())

    override val state = _state.asStateFlow()

    override fun dispatch(cocktailsUiEvent: CocktailEditUiEvent) {
        reduce(cocktailsUiEvent)
    }

    private fun reduce(event: CocktailEditUiEvent) {
        componentScope.launch {
            val stateFlow = _state
            when (event) {
                is SaveCocktail -> {
                    stateFlow.update { CocktailEditState(saveLoading = true) }
                    val state = stateFlow.value
                    addCocktailUseCase(
                        Cocktail(
                            id = null,
                            name = state.title,
                            description = state.description,
                            recipe = state.recipe,
                            ingredients = state.ingredients
                        )
                    )
                    stateFlow.update { CocktailEditState(saveLoading = false) }
                }

                is ChangeTitleValue -> {
                    stateFlow.update { it.copy(title = event.value) }
                }

                is ChangeDescriptionValue -> {
                    stateFlow.update { it.copy(description = event.value) }
                }

                is ChangeRecipeValue -> {
                    stateFlow.update { it.copy(recipe = event.value) }
                }

                is OnPickerResult -> {
                    val uri = event.uri ?: return@launch
                    stateFlow.value.image?.let {
                        launch(Dispatchers.IO) {
                            deleteCocktailImageUseCase(it)
                        }
                    }
                    stateFlow.update { it.copy(image = uri.toString()) }
                    saveCocktailImageUseCase(uri).collect { result ->
                        when (result) {
                            is DownloadState.Downloading -> stateFlow.update {
                                it.copy(
                                    imageLoaderProgressPercentage = result.progress
                                )
                            }
                            is DownloadState.Finished -> {
                                stateFlow.update {
                                    it.copy(image = result.value, imageLoaderProgressPercentage = 100)
                                }
                            }
                        }
                    }
                }

                is OnCocktailPictureLoaderCompleted -> {
                    stateFlow.update { it.copy(imageLoaderProgressPercentage = null) }
                }
            }
        }
    }
}