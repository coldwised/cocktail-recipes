package com.cocktailbar

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.cocktailbar.di.AppComponent
import com.cocktailbar.di.create
import com.cocktailbar.presentation.RootComponent
import com.cocktailbar.presentation.RootScreen
import com.cocktailbar.ui.theme.CocktailBarTheme
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
        val root = retainedComponent {
            ActivityComponent::class.create(
                AppComponent::class.create(
                    applicationContext
                )
            ).rootComponentCreator.create(it)
        }
        setContent {
            ChangeSystemBarsTheme(!isSystemInDarkTheme())
            CocktailBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootScreen(root = root)
                }
            }
        }
    }

    @Composable
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = TRANSPARENT
        DisposableEffect(lightTheme) {
            if (lightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                )
            } else {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                )
            }
            onDispose {}
        }
    }
}