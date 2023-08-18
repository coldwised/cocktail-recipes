package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.R
import com.cocktailbar.presentation.Cocktail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(
    onSelectCocktail: (Cocktail) -> Unit,
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "My cocktails"
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            if()
            CocktailsList(state.cocktailList)
        }
    }
}

@Composable
fun CocktailsList(cocktailsList: List<Cocktail>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
        items(cocktailsList) { cocktail ->
            CocktailItem(cocktail)
        }
    }
}

@Composable
fun CocktailItem(cocktail: Cocktail) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null
        )
        Text(
            text = cocktail.name
        )
    }
}