package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.cocktailbar.domain.model.Cocktail
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CocktailDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val cocktail: Cocktail,
    @Assisted private val navigateToEditCocktail: (Cocktail) -> Unit,
    @Assisted private val dismiss: () -> Unit,
    @Assisted private val deleteAndDismiss: (Cocktail) -> Unit,
) : ICocktailDetailsComponent, ComponentContext by componentContext {

    override fun onEditClick() {
        navigateToEditCocktail(cocktail)
    }

    override fun onDeleteClick() {
        deleteAndDismiss(cocktail)
    }

    private val backCallback = BackCallback {
        dismiss()
    }

    init {
        backHandler.register(backCallback)
    }
}