package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.R

@Composable
fun EditCocktailScreen(
    cocktailEditComponent: ICocktailEditComponent
) {
    val state = cocktailEditComponent.state.collectAsStateWithLifecycle().value
    val dispatch = cocktailEditComponent::dispatch
    val maxWidthModifier = remember { Modifier.fillMaxWidth() }
    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        bottomBar = {
            BottomBar(
                modifier = maxWidthModifier.height(50.dp),
                saveLoading = state.saveLoading,
                onSaveClick = { dispatch(SaveCocktail)}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val textFieldShape = remember { CircleShape }
            Spacer(modifier = Modifier.height(16.dp))
            Title(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.title,
                onQueryChange = {
                    dispatch(ChangeTitleValue(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Description(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.description,
                onQueryChange = {
                    dispatch(ChangeDescriptionValue(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Ingredients()
            Spacer(modifier = Modifier.height(16.dp))
            Recipe(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.recipe,
                onQueryChange = { dispatch(ChangeRecipeValue(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }   
    }
}

@Composable
fun BottomBar(
    modifier: Modifier,
    saveLoading: Boolean,
    onSaveClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = CircleShape,
        onClick = onSaveClick
    ) {
        if(saveLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = stringResource(R.string.save_button))
        }
    }
}

@Composable
fun Title(
    modifier: Modifier,
    shape: Shape,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = stringResource(R.string.cocktail_name))
        },
        supportingText = {
            Text(stringResource(R.string.add_title))
        },
        isError = query.isBlank()
    )
}

@Composable
fun Description(
    modifier: Modifier,
    shape: Shape,
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = stringResource(R.string.cocktail_description))
        },
        supportingText = {
            Text(stringResource(R.string.optional_field))
        }
    )
}

@Composable
fun Ingredients(
) {
}

@Composable
fun Recipe(
    modifier: Modifier,
    shape: Shape,
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = stringResource(R.string.cocktail_recipe))
        },
        supportingText = {
            Text(stringResource(R.string.optional_field))
        }
    )
}