package com.cocktailbar.presentation.cocktails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cocktailbar.R
import com.cocktailbar.util.toPx

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(cocktailsComponent: ICocktailsComponent) {
    val childSlot = cocktailsComponent.childSlot.collectAsStateWithLifecycle().value
    val sheetState = rememberStandardBottomSheetState()
    val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val child = childSlot.child
    val cocktailDetailsOpened = child != null
    val fabSize by animateDpAsState(
        targetValue = if (cocktailDetailsOpened) {
            0.dp
        } else 80.dp,
        label = "FAB"
    )
    val fabCutoutRadiusPx = (fabSize / 2 + 6.dp).toPx()
    val customShapeWithCutout = remember { customShapeWithCutout(fabCutoutRadiusPx, 300f) }
    val customShape = remember { customShapeWithCutout(0f, 300f) }
    var cocktailDetailImage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(cocktailDetailsOpened) {
        if (cocktailDetailsOpened) {
            sheetState.expand()
        } else {
            sheetState.partialExpand()
        }
    }
    Scaffold { systemBarsPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            BottomSheetScaffold(
                scaffoldState = sheetScaffoldState,
                sheetContainerColor = MaterialTheme.colorScheme.background,
                sheetShadowElevation = 4.dp,
                sheetPeekHeight = 56.dp + systemBarsPadding.calculateBottomPadding(),
                sheetContent = {
                    Box(
                        Modifier
                            .padding(bottom = systemBarsPadding.calculateBottomPadding())
                            .fillMaxWidth()
                            .height(478.dp)
                    ) {
                        if (cocktailDetailsOpened) {
                            when (val childInstance = child!!.instance) {
                                is ICocktailsComponent.SlotChild.CocktailDetailsChild -> {
                                    val component = childInstance.component
                                    cocktailDetailImage = component.cocktail.image
                                    CocktailDetails(component)
                                }
                            }
                        }
                    }
                },
                sheetSwipeEnabled = false,
                sheetDragHandle = null,
                sheetShape = if (cocktailDetailsOpened) customShape else customShapeWithCutout,
            ) { bottomSheetPaddingValues ->
                if (!cocktailDetailsOpened)
                    CocktailListScreen(
                        cocktailsComponent.cocktailListComponent,
                        bottomSheetPaddingValues.calculateBottomPadding()
                    )
                AnimatedBackgroundImage(cocktailDetailsOpened, cocktailDetailImage)
            }
            Column(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(systemBarsPadding), horizontalAlignment = CenterHorizontally
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(fabSize),
                    shape = CircleShape,
                    onClick = cocktailsComponent::navigateToCreateCocktail
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AnimatedBackgroundImage(cocktailDetailsOpened: Boolean, cocktailDetailImage: String?) {
    AnimatedVisibility(
        visible = cocktailDetailsOpened,
        enter = slideInVertically(
            tween(300)
        ) + fadeIn(tween(500)),
        exit = slideOutVertically(
            tween(700)
        ) + fadeOut(tween(200))
    ) {
        AsyncImage(
            model = cocktailDetailImage ?: R.drawable.cocktail_placeholder,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

private fun customShapeWithCutout(cutoutRadius: Float, cornerRadius: Float) = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            reset()
            arcTo(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = Size(cornerRadius, cornerRadius)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            if (cutoutRadius > 0f) {
                lineTo(x = (size.width / 2) - cutoutRadius, y = 0f)
                arcTo(
                    rect = Rect(
                        left = size.width / 2 - cutoutRadius,
                        top = -cutoutRadius,
                        right = size.width / 2 + cutoutRadius,
                        bottom = cutoutRadius
                    ),
                    startAngleDegrees = 180.0f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )
                lineTo(x = size.width - cornerRadius, y = 0f)
            } else {
                lineTo(x = size.width, y = 0f)
            }
            arcTo(
                rect = Rect(
                    offset = Offset(size.width - cornerRadius, 0f),
                    size = Size(cornerRadius, cornerRadius)
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
            lineTo(x = 0f, y = 0f)
            close()
        }
        return Outline.Generic(path)
    }
}