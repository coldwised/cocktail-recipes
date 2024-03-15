package com.cocktailbar.presentation.cocktails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cocktailbar.R
import com.cocktailbar.util.RoundedButtonShape
import com.cocktailbar.util.getImageCacheKey

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CocktailEditScreen(
    cocktailEditComponent: ICocktailEditComponent
) {
    val dispatch = cocktailEditComponent::dispatch
    val state by cocktailEditComponent.state.collectAsStateWithLifecycle()
    val pictureVerticalSpacerModifier = Modifier.height(64.dp)
    val verticalSpacerModifier = Modifier.height(16.dp)
    val focusManager = LocalFocusManager.current
    val imeIsVisible = WindowInsets.isImeVisible
    val textFieldShape = RoundedCornerShape(34.dp)
    val maxWidthModifier = Modifier.fillMaxWidth()
    LaunchedEffect(imeIsVisible) {
        if (!imeIsVisible) focusManager.clearFocus()
    }
    Scaffold(
        bottomBar = {
            val enabled by remember {
                derivedStateOf { state.imageLoaderProgressPercentage == null && state.title.isNotBlank() }
            }
            BottomBar(
                enabled = enabled,
                cancellationInProgress = { state.cancellationInProgress },
                onSaveClick = { dispatch(SaveCocktail) },
                onCancelClick = { dispatch(OnCancelClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = pictureVerticalSpacerModifier)
            CocktailImage(
                image = state.image,
                loaderProgress = state.imageLoaderProgressPercentage?.div(100f),
                onPickerResult = { dispatch(OnPickerResult(it)) },
                onCocktailPictureLoaderCompleted = { dispatch(OnCocktailPictureLoaderCompleted) }
            )
            Spacer(modifier = pictureVerticalSpacerModifier)

            val focusManagerProvider = remember { { focusManager } }
            Title(
                modifier = maxWidthModifier,
                query = state.title,
                focusManagerProvider = focusManagerProvider,
                shape = textFieldShape,
                onQueryChange =
                { dispatch(ChangeTitleValue(it)) }

            )
            Spacer(modifier = verticalSpacerModifier)
            Description(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.description,
                focusManagerProvider = focusManagerProvider,
                onQueryChange = { dispatch(ChangeDescriptionValue(it)) }
            )
            Spacer(modifier = verticalSpacerModifier)
            Ingredients(
                ingredients = state.ingredients,
                onAddIngredientClick = remember { { dispatch(OnAddIngredientClicked) } },
                onRemoveIngredient = remember { { dispatch(RemoveIngredient(it)) } }
            )
            Spacer(modifier = verticalSpacerModifier)
            Recipe(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.recipe,
                onQueryChange = { dispatch(ChangeRecipeValue(it)) }
            )
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding() + 16.dp))
        }
    }
}

@Composable
private fun CocktailImage(
    image: String?,
    loaderProgress: Float?,
    onPickerResult: (Uri?) -> Unit,
    onCocktailPictureLoaderCompleted: () -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onPickerResult
    )
    Box {
        val imageCacheKey = remember(image) { image?.let { getImageCacheKey(it) } }
        val coilPainter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(image ?: R.drawable.cocktail_placeholder)
                .crossfade(true)
                .memoryCacheKey(imageCacheKey)
                .placeholderMemoryCacheKey(imageCacheKey)
                .build(),
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(54.dp))
                .clickable(
                    enabled = loaderProgress == null,
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ),
            painter = coilPainter,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        val animatedProgress by animateFloatAsState(
            targetValue = loaderProgress ?: 0f,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            label = "",
            finishedListener = { progress ->
                if (progress >= 1f) onCocktailPictureLoaderCompleted()
            }
        )
        if (image != null && loaderProgress != null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                progress = { animatedProgress },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface,
                strokeWidth = 6.dp
            )
        }
    }
}

@Composable
private fun BottomBar(
    enabled: Boolean,
    cancellationInProgress: () -> Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
    ) {
        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
        val progressBarModifier = Modifier.size(20.dp)

        Button(
            modifier = buttonModifier,
            shape = RoundedButtonShape,
            enabled = enabled,
            onClick = onSaveClick
        ) {
            Text(
                text = stringResource(R.string.save_button),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedButton(
            modifier = buttonModifier,
            shape = RoundedButtonShape,
            onClick = onCancelClick,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(1.0.dp, MaterialTheme.colorScheme.primary)
        ) {
            if (cancellationInProgress()) {
                CircularProgressIndicator(
                    modifier = progressBarModifier,
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            } else {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun Title(
    modifier: Modifier,
    shape: Shape,
    query: String,
    focusManagerProvider: () -> FocusManager,
    onQueryChange: (String) -> Unit,
) {
    val isError = query.isBlank()
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(text = stringResource(R.string.cocktail_name))
        },
        label = {
            Text(stringResource(R.string.title))
        },
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.add_title))
            }
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManagerProvider().moveFocus(FocusDirection.Down)
            }
        )
    )
}

@Composable
private fun Description(
    modifier: Modifier,
    shape: Shape,
    query: String,
    focusManagerProvider: () -> FocusManager,
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
        },
        label = {
            Text(stringResource(R.string.description))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManagerProvider().moveFocus(FocusDirection.Down)
            }
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Ingredients(
    ingredients: List<String>,
    onAddIngredientClick: () -> Unit,
    onRemoveIngredient: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.ingredients)
    )
    Spacer(Modifier.height(8.dp))
    val flowRowModifier = Modifier.height(24.dp)
    FlowRow {
        val iconButtonModifier = Modifier.size(16.dp)
        val closeIcon = Icons.Default.Close
        val circleShape = CircleShape
        val labelSmallStyle = MaterialTheme.typography.labelSmall
        val chipHorizontalSpacerModifier = Modifier.width(6.dp)
        val chipSpacerVerticalModifier = Modifier.height(6.dp)
        ingredients.fastForEachIndexed { index, ingredient ->
            Column {
                AssistChip(
                    modifier = flowRowModifier,
                    onClick = {},
                    shape = circleShape,
                    trailingIcon = {
                        IconButton(
                            modifier = iconButtonModifier,
                            onClick = { onRemoveIngredient(index) }
                        ) {
                            Icon(
                                imageVector = closeIcon,
                                contentDescription = null
                            )
                        }
                    },
                    label = { Text(text = ingredient, style = labelSmallStyle) }
                )
                Spacer(chipSpacerVerticalModifier)
            }
            Spacer(chipHorizontalSpacerModifier)
        }
        IconButton(
            modifier = Modifier.size(24.dp), onClick = onAddIngredientClick,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Recipe(
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
        },
        label = {
            Text(stringResource(R.string.recipe))
        },
    )
}