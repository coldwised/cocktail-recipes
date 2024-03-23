package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.cocktailbar.di.MainImmediateDispatcher
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.DeleteCocktailImageUseCase
import com.cocktailbar.domain.use_case.SaveCocktailImageUseCase
import com.cocktailbar.util.DownloadState
import com.cocktailbar.util.coroutineScope
import kotlinx.coroutines.Job
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
    @Assisted private val cocktail: Cocktail?,
    @Assisted private val openIngredientDialog: () -> Unit,
    @Assisted private val saveAndDismissCocktail: (Cocktail) -> Unit,
    @Assisted private val navigateBack: () -> Unit,
    mainImmediateDispatcher: MainImmediateDispatcher,
    private val saveCocktailImageUseCase: SaveCocktailImageUseCase,
    private val deleteCocktailImageUseCase: DeleteCocktailImageUseCase,
) : ComponentContext by componentContext, ICocktailEditComponent {

    private val componentScope = coroutineScope(mainImmediateDispatcher + SupervisorJob())

    private val _state = MutableStateFlow(
        stateKeeper.consume(key = "COCKTAIL_EDIT_STATE", strategy = CocktailEditState.serializer()) ?: cocktail?.let {
            CocktailEditState(
                image = it.image,
                title = it.name,
                description = it.description.orEmpty(),
                recipe = it.recipe.orEmpty(),
                ingredients = it.ingredients.orEmpty(),
                cachedExistingCocktailImage = it.image,
            )
        } ?: CocktailEditState())

    override val state = _state.asStateFlow()

    init {
        stateKeeper.register(key = "COCKTAIL_EDIT_STATE", strategy = CocktailEditState.serializer()) { _state.value }
    }

    override fun dispatch(cocktailsUiEvent: CocktailEditUiEvent) {
        reduce(cocktailsUiEvent)
    }

    private var imageLoadingJob: Job? = null

    private fun reduce(event: CocktailEditUiEvent) {
        componentScope.launch {
            val stateFlow = _state
            when (event) {
                is SaveCocktail -> {
                    val state = stateFlow.value
                    state.cachedExistingCocktailImage?.takeIf { it != state.image }
                        ?.let { deleteCocktailImageUseCase(it) }
                    saveAndDismissCocktail(
                        Cocktail(
                            id = cocktail?.id,
                            name = state.title,
                            description = state.description.ifBlank { null },
                            recipe = state.recipe.ifBlank { null },
                            ingredients = state.ingredients.ifEmpty { null },
                            image = state.image,
                        )
                    )
                }

                is OnCancelClick -> {
                    val stateValue = stateFlow.value
                    if (stateValue.imageLoaderProgressPercentage != null) {
                        stateFlow.update { it.copy(cancellationInProgress = true) }
                        imageLoadingJob?.join()
                        stateValue.image?.let { image ->
                            if (image != stateValue.cachedExistingCocktailImage) {
                                deleteCocktailImageUseCase(image)
                            }
                        }
                        navigateBack()
                    } else {
                        stateValue.image?.let { image ->
                            if (image != stateValue.cachedExistingCocktailImage) {
                                deleteCocktailImageUseCase(image)
                            }
                        }
                        navigateBack()
                    }
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
                    val stateValue = stateFlow.value
                    stateValue.image?.let {
                        if (stateValue.cachedExistingCocktailImage != it) {
                            deleteCocktailImageUseCase(it)
                        }
                    }
                    stateFlow.update { it.copy(image = uri.toString()) }
                    imageLoadingJob = componentScope.launch {
                        saveCocktailImageUseCase(uri).collect { result ->
                            when (result) {
                                is DownloadState.Downloading -> stateFlow.update {
                                    it.copy(
                                        imageLoaderProgressPercentage = result.progress
                                    )
                                }

                                is DownloadState.Finished -> {
                                    stateFlow.update {
                                        it.copy(
                                            image = result.value,
                                            imageLoaderProgressPercentage = 100
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is OnCocktailPictureLoaderCompleted -> {
                    stateFlow.update { it.copy(imageLoaderProgressPercentage = null) }
                }

                is SaveIngredientValue -> {
                    stateFlow.update { it.copy(ingredients = it.ingredients.plus(event.ingredient)) }
                }

                is OnAddIngredientClicked -> {
                    openIngredientDialog()
                }

                is RemoveIngredient -> {
                    stateFlow.update { it.copy(ingredients = it.ingredients.filterIndexed { index, _ -> index != event.index }) }
                }
            }
        }
    }
}