package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cocktailbar.R
import com.cocktailbar.domain.model.Cocktail

@Composable
fun CocktailListScreen(cocktailListComponent: ICocktailListComponent) {
    val state = cocktailListComponent.state.collectAsStateWithLifecycle().value
    Box(modifier = Modifier
        .statusBarsPadding()
        .fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.cocktails.isNotEmpty()) {
            CocktailsGrid(
                cocktails = state.cocktails,
                onItemClicked = { cocktailListComponent.dispatch(CocktailsEvent.OnCocktailClicked(it)) }
            )
        }
        val clickedCocktailImage = state.clickedCocktailImage
        AnimatedVisibility(visible = clickedCocktailImage != null) {
            AsyncImage(
                model = state.clickedCocktailImage,
                placeholder = painterResource(id = R.drawable.cocktail_placeholder),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}


@Composable
fun CocktailsGrid(
    cocktails: List<Cocktail>,
    onItemClicked: (Cocktail) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = cocktails
        ) { cocktail ->
            CocktailItem(
                cocktail = cocktail,
                onItemClicked = onItemClicked
            )
        }
    }
}

@Composable
fun CocktailItem(
    cocktail: Cocktail,
    onItemClicked: (Cocktail) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(54.dp))
            .clickable {
                onItemClicked(cocktail)
            }

    ) {
        val placeHolderId = remember { R.drawable.cocktail_placeholder }
        AsyncImage(
            model = cocktail.image ?: placeHolderId,
            placeholder = painterResource(id = placeHolderId),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cocktail.name,
                color = Color.White,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}