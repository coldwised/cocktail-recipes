package com.cocktailbar.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.cocktailbar.presentation.Cocktail
import com.cocktailbar.presentation.cocktail_details.CocktailDetailsScreen
import com.cocktailbar.presentation.cocktails.CocktailsScreen
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Screen: Parcelable {
    object CocktailsScreen: Screen()
    data class CocktailDetailsScreen(val cocktail: Cocktail): Screen()
}

@Composable
fun CocktailsHoistScreen() {
    val router: Router<Screen> = rememberRouter(Screen::class, listOf(Screen.CocktailsScreen))

    RoutedContent(
        router = router,
        modifier = Modifier.fillMaxSize(),
        animation = stackAnimation(slide()),
    ) { screen ->
        when(screen) {
            is Screen.CocktailsScreen -> CocktailsScreen(
                onSelectCocktail = { cocktail -> router.push(Screen.CocktailDetailsScreen(cocktail)) }
            )
            is Screen.CocktailDetailsScreen -> CocktailDetailsScreen()
        }
    }
}
