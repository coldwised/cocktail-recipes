package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.domain.model.Cocktail

@Composable
fun CocktailListScreen(cocktailList: ICocktailListComponent) {
    val state = cocktailList.state.collectAsStateWithLifecycle().value
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.cocktails.isNotEmpty()) {

        }
    }
}


@Composable
fun CocktailsGrid(cocktails: List<Cocktail>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp)
    ) {
        items(
            items = cocktails
        ) {

        }
    }
}

@Composable
fun CocktailItem(cocktail: Cocktail) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
    ) {
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = cocktail.name
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}