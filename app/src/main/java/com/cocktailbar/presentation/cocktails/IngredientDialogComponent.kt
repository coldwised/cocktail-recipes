package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class IngredientDialogComponent(
    componentContext: ComponentContext,
    private val dismissDialog: () -> Unit,
    private val saveIngredient: (String) -> Unit
): ComponentContext by componentContext, IIngredientDialogComponent {
    private val _state = MutableStateFlow(IngredientDialogState())
    override val state = _state.asStateFlow()
    override fun dispatch(ingredientDialogEvent: IngredientDialogEvent) {
        reduce(ingredientDialogEvent)
    }

    private fun reduce(event: IngredientDialogEvent) {
        val stateFlow = _state
        when(event) {
            IngredientDialogEvent.OnDismiss -> {
                dismissDialog()
            }
            is IngredientDialogEvent.OnIngredientTextChanged -> {
                stateFlow.update {
                    it.copy(ingredientText = event.value)
                }
            }
            IngredientDialogEvent.OnSaveIngredient -> {
                saveIngredient(stateFlow.value.ingredientText)
                dismissDialog()
            }
        }
    }
}