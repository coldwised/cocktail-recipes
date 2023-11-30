package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocktailbar.R

@Composable
fun CocktailDetails(cocktailDetails: ICocktailDetailsComponent) {
    val cocktail = cocktailDetails.cocktail
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(cocktail.name)
        Text(cocktail.description)
        for (ingredient in cocktail.ingredients) {
            Text(ingredient)
            Divider(Modifier.width(16.dp))
        }
        cocktail.recipe.takeIf { it.isNotBlank() }?.let {
            Text(stringResource(R.string.recipe_colon))
            Text(it)
        }
        Button(
            modifier = Modifier.fillMaxWidth().height(55.dp),
            onClick = cocktailDetails::onEditClick,
            shape = CircleShape
        ) {
            Text(stringResource(R.string.edit))
        }
    }
}