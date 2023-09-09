package com.cocktailbar.presentation.cocktails

import com.arkivanov.decompose.ComponentContext

class CocktailEditComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext, ICocktailEditComponent {
}