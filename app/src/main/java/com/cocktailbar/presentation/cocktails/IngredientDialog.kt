package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.R
import com.cocktailbar.util.RoundedButtonShape

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
        tonalElevation = 0.dp,
        onDismissRequest = { dispatch(IngredientDialogEvent.OnDismiss) },
        confirmButton = {
            OutlinedButton(
                modifier = buttonModifier,
                shape = RoundedButtonShape,
                border = BorderStroke(1.0.dp, MaterialTheme.colorScheme.primary),
                onClick = { dispatch(IngredientDialogEvent.OnDismiss) }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        shape = RoundedCornerShape(54.dp),
        dismissButton = {
            Button(
                modifier = buttonModifier,
                onClick = { dispatch(IngredientDialogEvent.OnSaveIngredient) },
                shape = RoundedButtonShape,
                enabled = !isBlankText
            ) {
                Text(
                    text = stringResource(R.string.add),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.add_ingredient_title),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            OutlinedTextField(
                modifier = Modifier
                    .animateContentSize()
                    .focusRequester(focusRequester),
                value = ingredientText,
                shape = RoundedCornerShape(34.dp),
                onValueChange = { dispatch(IngredientDialogEvent.OnIngredientTextChanged(it)) },
                placeholder = {
                    Text(text = stringResource(R.string.ingredient_name_placeholder))
                },
                label = { Text(stringResource(R.string.ingredient)) },
                singleLine = true,
                supportingText = if (isBlankText) {
                    { Text(stringResource(R.string.add_name)) }
                } else null,
                isError = isBlankText
            )
        }
    )
}