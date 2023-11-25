package com.cocktailbar.presentation.cocktails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cocktailbar.R

@Composable
fun CocktailEditScreen(
    cocktailEditComponent: ICocktailEditComponent
) {
    val state = cocktailEditComponent.state.collectAsStateWithLifecycle().value
    val dispatch = cocktailEditComponent::dispatch
    val maxWidthModifier = remember { Modifier.fillMaxWidth() }
    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        bottomBar = {
            BottomBar(
                enabled = state.imageLoaderProgressPercentage == null && state.title.isNotBlank(),
                saveLoading = state.saveLoading,
                cancelLoading = state.removePictureLoading,
                onSaveClick = { dispatch(SaveCocktail) },
                onCancelClick = { dispatch(OnCancelClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            val pictureVerticalSpacerModifier = remember { Modifier.height(64.dp) }
            val verticalSpacerModifier = remember { Modifier.height(16.dp) }
            Spacer(modifier = pictureVerticalSpacerModifier)
            val textFieldShape = remember { CircleShape }
            CocktailImage(
                loaderProgress = state.imageLoaderProgressPercentage?.div(100f),
                image = state.image,
                onPickerResult = { dispatch(OnPickerResult(it)) },
                onCocktailPictureLoaderCompleted = { dispatch(OnCocktailPictureLoaderCompleted) }
            )
            Spacer(modifier = pictureVerticalSpacerModifier)
            Title(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.title,
                onQueryChange = {
                    dispatch(ChangeTitleValue(it))
                }
            )
            Spacer(modifier = verticalSpacerModifier)
            Description(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.description,
                onQueryChange = {
                    dispatch(ChangeDescriptionValue(it))
                }
            )
            Spacer(modifier = verticalSpacerModifier)
            Ingredients(
                ingredients = state.ingredients,
                onAddIngredientClick = { dispatch(OnAddIngredientClicked) },
                { dispatch(RemoveIngredient(it)) }
            )
            Spacer(modifier = verticalSpacerModifier)
            Recipe(
                modifier = maxWidthModifier,
                shape = textFieldShape,
                query = state.recipe,
                onQueryChange = { dispatch(ChangeRecipeValue(it)) }
            )
            Spacer(modifier = verticalSpacerModifier)
        }
    }
}

@Composable
fun CocktailImage(
    loaderProgress: Float?,
    image: String?,
    onPickerResult: (Uri?) -> Unit,
    onCocktailPictureLoaderCompleted: () -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onPickerResult
    )
    Box {
        val coilPainter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(image ?: R.drawable.cocktail_placeholder)
                .crossfade(true)
                .memoryCacheKey(image?.split('/')?.last()?.substringBefore('.'))
                .build(),
            contentScale = ContentScale.FillBounds
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(60.dp))
                .clickable(
                    enabled = loaderProgress == null,
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                .background(Color.LightGray),
            painter = coilPainter,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
        if (image != null && loaderProgress != null) {
            val animatedProgress by animateFloatAsState(
                targetValue = loaderProgress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                label = "",
                finishedListener = { progress -> if (progress >= 1f) onCocktailPictureLoaderCompleted() }
            )
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                progress = animatedProgress
            )
        }
    }
}

@Composable
fun BottomBar(
    saveLoading: Boolean,
    cancelLoading: Boolean,
    enabled: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column {
        val buttonModifier = remember { Modifier.fillMaxWidth().height(55.dp) }
        val progressBarModifier = remember { Modifier.size(20.dp) }
        Button(
            modifier = buttonModifier,
            shape = CircleShape,
            enabled = enabled,
            onClick = onSaveClick
        ) {
            if (saveLoading) {
                CircularProgressIndicator(
                    modifier = progressBarModifier,
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = stringResource(R.string.save_button))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            modifier = buttonModifier,
            shape = CircleShape,
            onClick = onCancelClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            )
        ) {
            if (cancelLoading) {
                CircularProgressIndicator(
                    modifier = progressBarModifier,
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            } else {
                Text(text = stringResource(R.string.cancel))
            }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ingredients(
    ingredients: List<String>,
    onAddIngredientClick: () -> Unit,
    onRemoveIngredient: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.ingredients)
    )
    Spacer(Modifier.height(8.dp))
    val flowRowModifier = Modifier.height(24.dp)
    FlowRow(
        verticalAlignment = CenterVertically
    ) {
        val iconButtonModifier = remember { Modifier.size(16.dp) }
        val closeIcon = remember { Icons.Default.Close }
        val circleShape = remember { CircleShape }
        val labelSmallStyle = MaterialTheme.typography.labelSmall
        val chipSpacerModifier = remember { Modifier.width(6.dp) }
        ingredients.forEachIndexed { index, ingredient ->
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
            Spacer(chipSpacerModifier)
        }
        IconButton(
            modifier = Modifier.size(24.dp), onClick = onAddIngredientClick
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null
            )
        }
    }
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