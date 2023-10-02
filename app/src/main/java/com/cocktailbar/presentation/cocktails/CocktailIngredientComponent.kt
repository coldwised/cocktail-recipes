package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext

class CocktailIngredientComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext, ICocktailIngredientComponent {
}