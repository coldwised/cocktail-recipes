package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cocktailbar.R
import com.cocktailbar.util.RoundedButtonShape

@Composable
fun CocktailDetails(cocktailDetails: ICocktailDetailsComponent) {
    val cocktail = cocktailDetails.cocktail
    Scaffold(
        bottomBar = {
            BottomBar(
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
            val textCenterAlignment = TextAlign.Center
            Spacer(modifier = Modifier.height(27.dp))
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = textCenterAlignment,
            )
            cocktail.description?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    textAlign = textCenterAlignment
                )
            }
            cocktail.ingredients?.let { ingredients ->
                Spacer(modifier = Modifier.height(32.dp))
                val lastIndex = ingredients.lastIndex
                for (index in ingredients.indices) {
                    Text(
                        text = ingredients[index],
                        textAlign = textCenterAlignment
                    )
                    if (index != lastIndex)
                        HorizontalDivider(
                            Modifier
                                .padding(vertical = 16.dp)
                                .width(9.dp)
                                .height(1.dp)
                        )
                }
            }
            cocktail.recipe?.let { recipe ->
                if (recipe.isNotBlank()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.recipe_colon),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = textCenterAlignment
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recipe,
                        textAlign = textCenterAlignment
                    )
                }
            }
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 24.dp))
        }
    }
}

@Composable
private fun BottomBar(
    onEditClicked: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {
        val roundedButtonShape = RoundedButtonShape
        val headlineSmallTextStyle = MaterialTheme.typography.headlineSmall
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            onClick = onEditClicked,
            shape = roundedButtonShape,
        ) {
            Text(
                text = stringResource(R.string.edit),
                style = headlineSmallTextStyle
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(55.dp),
            onClick = onDeleteClick,
            shape = roundedButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(R.string.delete),
                style = headlineSmallTextStyle
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}