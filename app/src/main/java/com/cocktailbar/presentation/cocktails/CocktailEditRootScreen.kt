package com.cocktailbar.presentation.cocktails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CocktailEditRootScreen(cocktailEditRootComponent: ICocktailEditRootComponent) {
    val childSlot by cocktailEditRootComponent.childSlot.collectAsStateWithLifecycle()
    CocktailEditScreen(cocktailEditComponent = cocktailEditRootComponent.cocktailEditComponent)
    childSlot.child?.instance?.let { instance ->
        when (instance) {
            is ICocktailEditRootComponent.SlotChild.CocktailIngredient -> {
                IngredientDialog(ingredientDialogComponent = instance.component)
            }
        }
    }
}