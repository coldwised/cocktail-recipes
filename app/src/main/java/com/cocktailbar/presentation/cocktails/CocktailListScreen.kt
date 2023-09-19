package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CocktailListScreen(cocktailList: ICocktailListComponent) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("cocktails list")
    }
}