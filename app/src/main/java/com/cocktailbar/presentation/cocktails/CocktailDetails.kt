package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.R

@Composable
fun CocktailDetails(cocktailDetails: ICocktailDetailsComponent) {
    val cocktail = cocktailDetails.cocktail
    val isDeleteInProcess = cocktailDetails.isDeleteInProcess.collectAsStateWithLifecycle().value
    Scaffold(
        bottomBar = {
            BottomBar(
                isDeleteInProcess,
                cocktailDetails::onEditClick,
                cocktailDetails::onDeleteClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(27.dp))
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            val description = cocktail.description
            if(description.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    textAlign = TextAlign.Center
                )
            }
            val ingredients = cocktail.ingredients
            if(ingredients.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                val lastIndex = ingredients.lastIndex
                for (index in ingredients.indices) {
                    Text(
                        text = ingredients[index],
                        textAlign = TextAlign.Center
                    )
                    if(index != lastIndex)
                        Divider(
                            Modifier
                                .padding(vertical = 16.dp)
                                .width(9.dp)
                                .height(1.dp))
                }
            }
            cocktail.recipe.takeIf { it.isNotBlank() }?.let {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.recipe_colon),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 24.dp))
        }
    }
}

@Composable
private fun BottomBar(
    isDeleteInProcess: Boolean,
    onEditClicked: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            onClick = onEditClicked,
            shape = CircleShape
        ) {
            Text(
                text = stringResource(R.string.edit),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            onClick = onDeleteClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            if(isDeleteInProcess) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}