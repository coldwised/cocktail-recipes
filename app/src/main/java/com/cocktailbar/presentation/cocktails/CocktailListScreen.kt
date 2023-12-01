package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cocktailbar.R
import com.cocktailbar.domain.model.Cocktail

@Composable
fun CocktailListScreen(cocktailListComponent: ICocktailListComponent, bottomPadding: Dp) {
    val state = cocktailListComponent.state.collectAsStateWithLifecycle().value
    val cocktailList = state.cocktails
    Scaffold(
        topBar = {
            TopBar(cocktailList.isNotEmpty())
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.cocktails.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CocktailsGrid(
                        cocktails = state.cocktails,
                        bottomPadding = bottomPadding,
                        topPadding = paddingValues.calculateTopPadding(),
                        onItemClicked = { cocktail, clickedCocktailImage ->
                            cocktailListComponent.dispatch(CocktailsEvent.OnCocktailClicked(cocktail, clickedCocktailImage))
                        }
                    )
                }
            } else {
                CocktailListPlaceholder(bottomPadding)
            }
        }
        val clickedCocktailImage = state.clickedCocktailImage
        val imageExpanded = clickedCocktailImage != null
        var cachedClickedCocktailImage by remember { mutableStateOf(clickedCocktailImage) }
        LaunchedEffect(key1 = imageExpanded) {
            if(imageExpanded) cachedClickedCocktailImage = clickedCocktailImage
        }
        AnimatedVisibility(
            visible = imageExpanded,
            enter = slideInVertically(
                tween(400)
            ) + fadeIn(tween(500)),
            exit = slideOutVertically(
                tween(700)
            ) + fadeOut(tween(200))
        ) {
            AsyncImage(
                model = cachedClickedCocktailImage,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TopBar(showTopBar: Boolean) {
    if(showTopBar) {
        val topBarColor = remember {Color(0xFFFF88BB) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            topBarColor,
                            topBarColor.copy(alpha = 0.9f),
                            topBarColor.copy(alpha = 0.8f),
                            topBarColor.copy(alpha = 0.7f),
                            topBarColor.copy(alpha = 0.6f),
                            topBarColor.copy(alpha = 0.5f),
                            topBarColor.copy(alpha = 0.4f),
                            topBarColor.copy(alpha = 0.3f),
                            topBarColor.copy(alpha = 0.2f),
                            topBarColor.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                    ))
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.my_cocktails),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CocktailListPlaceholder(bottomPadding: Dp) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(bottom = bottomPadding)
            .fillMaxSize()
            .padding(horizontal = 38.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val tipColor = MaterialTheme.colorScheme.outlineVariant
        Spacer(modifier = Modifier.height(33.dp))
        Image(
            modifier = Modifier.size(283.dp),
            painter = painterResource(id = R.drawable.summer_holidays),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.my_cocktails),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.width(130.dp),
            text = stringResource(R.string.add_first_cocktail),
            textAlign = TextAlign.Center,
            color = tipColor
        )
        Spacer(modifier = Modifier.height(36.dp))
        Icon(
            painter = painterResource(id = R.drawable.arrow_down),
            contentDescription = null,
            tint = tipColor
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun CocktailsGrid(
    cocktails: List<Cocktail>,
    bottomPadding: Dp,
    topPadding: Dp,
    onItemClicked: (Cocktail, Any) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            Spacer(modifier = Modifier.height(topPadding))
        }
        items(
            items = cocktails
        ) { cocktail ->
            CocktailItem(
                cocktail = cocktail,
                onItemClicked = onItemClicked
            )
        }
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            Spacer(modifier = Modifier.height(bottomPadding))
        }
    }
}

@Composable
private fun CocktailItem(
    cocktail: Cocktail,
    onItemClicked: (Cocktail, Any) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(54.dp))
            .clickable {
                onItemClicked(cocktail, cocktail.image ?: R.drawable.cocktail_placeholder)
            }
    ) {
        val placeHolderId = remember { R.drawable.cocktail_placeholder }
        AsyncImage(
            model = cocktail.image ?: placeHolderId,
            placeholder = painterResource(id = placeHolderId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.5f)
                        ),
                        startY = 0.0f,
                    )
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cocktail.name,
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}