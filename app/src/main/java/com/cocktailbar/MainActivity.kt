package com.cocktailbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.cocktailbar.di.AppComponent
import com.cocktailbar.di.create
import com.cocktailbar.presentation.RootComponent
import com.cocktailbar.presentation.RootScreen
import com.cocktailbar.ui.theme.CocktailBarTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Inject

@Inject
class RootComponentCreator(
    private val rootComponentFactory: (ComponentContext) -> RootComponent,
) {
    fun create(componentContext: ComponentContext): RootComponent {
        return rootComponentFactory(componentContext)
    }
}

@Component
abstract class ActivityComponent(@Component val appComponent: AppComponent) {
    abstract val rootComponentCreator: RootComponentCreator
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val root = retainedComponent {
            ActivityComponent::class.create(
                AppComponent::class.create(
                    applicationContext
                )
            ).rootComponentCreator.create(it)
        }
        setContent {
            TransparentSystemBars(isSystemInDarkTheme())
            CocktailBarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootScreen(root = root)
                }
            }
        }
    }
}

@Composable
fun TransparentSystemBars(isDarkTheme: Boolean) {
    val systemUiController = rememberSystemUiController()
    val transparentColor = Color.Transparent

    SideEffect {
        systemUiController.setNavigationBarColor(
            darkIcons = !isDarkTheme,
            color = transparentColor,
            navigationBarContrastEnforced = false,
        )
        systemUiController.setStatusBarColor(
            color = transparentColor,
            darkIcons = !isDarkTheme
        )
    }
}