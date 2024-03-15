package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cocktailbar.util.toPx

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(cocktailsComponent: ICocktailsComponent) {
    val childSlot by cocktailsComponent.childSlot.collectAsStateWithLifecycle()
    val sheetState = rememberStandardBottomSheetState()
    val child = childSlot.child
    val cocktailDetailsOpened = remember(child) {
        child != null
    }

    val fabCutoutRadiusPx = (80.dp / 2 + 6.dp).toPx()
    val shapeCornerRadius = 120.dp.toPx()
    val customShapeWithCutout =
        remember { customShapeWithCutout(fabCutoutRadiusPx, shapeCornerRadius) }
    val customShape = remember { customShapeWithCutout(0f, shapeCornerRadius) }
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
                scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState),
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
                                    CocktailDetails(childInstance.component)
                                }
                            }
                        }
                    }
                },
                sheetSwipeEnabled = false,
                sheetDragHandle = null,
                sheetShape = if (cocktailDetailsOpened) customShape else customShapeWithCutout,
            ) { bottomSheetPaddingValues ->
                CocktailListScreen(
                    cocktailsComponent.cocktailListComponent,
                    bottomSheetPaddingValues.calculateBottomPadding()
                )
                CocktailImage(cocktailsComponent.cocktailDetailImageComponent)
            }
            Column(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(systemBarsPadding), horizontalAlignment = CenterHorizontally
            ) {
                CocktailFab(cocktailsComponent.fabComponent)
                Spacer(Modifier.height(16.dp))
            }
        }
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