package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.R

@Composable
fun IngredientDialog(ingredientDialogComponent: IIngredientDialogComponent) {
    val state = ingredientDialogComponent.state.collectAsStateWithLifecycle().value
    val dispatch = ingredientDialogComponent::dispatch
    val buttonModifier = remember {
        Modifier
            .fillMaxWidth()
            .height(55.dp)
    }
    val ingredientText = state.ingredientText
    val isBlankText = ingredientText.isBlank()
    AlertDialog(
        onDismissRequest = { dispatch(IngredientDialogEvent.OnDismiss) },
        confirmButton = {
            OutlinedButton(
                modifier = buttonModifier,
                onClick = { dispatch(IngredientDialogEvent.OnDismiss) }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        dismissButton = {
            Button(
                modifier = buttonModifier,
                onClick = { dispatch(IngredientDialogEvent.OnSaveIngredient) },
                enabled = !isBlankText
            ) {
                Text(stringResource(R.string.add))
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.add_ingredient_title)
            )
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.animateContentSize(),
                value = ingredientText,
                shape = CircleShape,
                onValueChange = { dispatch(IngredientDialogEvent.OnIngredientTextChanged(it)) },
                placeholder = {
                    Text(text = stringResource(R.string.ingredient_name_placeholder))
                },
                singleLine = true,
                supportingText = if (isBlankText) {
                    { Text(stringResource(R.string.add_name)) }
                } else null,
                isError = isBlankText
            )
        }
    )
}