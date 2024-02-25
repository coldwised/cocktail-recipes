package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.cocktailbar.domain.model.Cocktail
import com.cocktailbar.domain.use_case.DeleteCocktailImageUseCase
import com.cocktailbar.domain.use_case.SaveCocktailImageUseCase
import com.cocktailbar.domain.use_case.SaveCocktailUseCase
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
    @Assisted private val cocktail: Cocktail?,
    @Assisted private val openIngredientDialog: () -> Unit,
    @Assisted private val navigateToCocktailsWithRefresh: () -> Unit,
    @Assisted private val navigateBack: () -> Unit,
    private val saveCocktailUseCase: SaveCocktailUseCase,
    private val saveCocktailImageUseCase: SaveCocktailImageUseCase,
    private val deleteCocktailImageUseCase: DeleteCocktailImageUseCase,
) : ComponentContext by componentContext, ICocktailEditComponent {
    private val componentScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val _state = MutableStateFlow(
        stateKeeper.consume(key = "COCKTAIL_EDIT_STATE") ?: cocktail?.let {
            CocktailEditState(
                image = it.image,
                title = it.name,
                description = it.description,
                recipe = it.recipe,
                ingredients = it.ingredients,
                cachedExistingCocktailImage = it.image,
            )
        } ?: CocktailEditState())

    override val state = _state.asStateFlow()

    init {
        stateKeeper.register(key = "COCKTAIL_EDIT_STATE") { _state.value }
    }

    override fun dispatch(cocktailsUiEvent: CocktailEditUiEvent) {
        reduce(cocktailsUiEvent)
    }

    private fun reduce(event: CocktailEditUiEvent) {
        componentScope.launch {
            val stateFlow = _state
            when (event) {
                is SaveCocktail -> {
                    stateFlow.update { it.copy(savingInProgress = true) }
                    val state = stateFlow.value
                    state.cachedExistingCocktailImage?.takeIf { it != state.image }
                        ?.let { deleteCocktailImageUseCase(it) }
                    saveCocktailUseCase(
                        Cocktail(
                            id = cocktail?.id,
                            name = state.title,
                            description = state.description,
                            recipe = state.recipe,
                            ingredients = state.ingredients,
                            image = state.image,
                        )
                    )
                    stateFlow.update { it.copy(savingInProgress = false) }
                    navigateToCocktailsWithRefresh()
                }

                is OnCancelClick -> {
                    val stateValue = stateFlow.value
                    stateValue.image?.let { image ->
                        if (cocktail == null || image != stateValue.cachedExistingCocktailImage) {
                            stateFlow.update { it.copy(cancellationInProgress = true, image = null) }
                            deleteCocktailImageUseCase(image)
                        }
                    }
                    navigateBack()
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
                            launch(Dispatchers.IO) {
                                deleteCocktailImageUseCase(it)
                            }
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
                                    it.copy(
                                        image = result.value,
                                        imageLoaderProgressPercentage = 100
                                    )
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