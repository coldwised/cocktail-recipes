package com.cocktailbar.presentation.cocktails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(cocktailsComponent: ICocktailsComponent) {
    val childSlot = cocktailsComponent.childSlot.collectAsStateWithLifecycle().value
    val childStack = cocktailsComponent.childStack.collectAsStateWithLifecycle().value
    val sheetState = rememberStandardBottomSheetState()
    val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetContent = {
            val child = childSlot.child
            LaunchedEffect(child == null) {
                if (child == null) {
                    sheetState.partialExpand()
                } else {
                    sheetState.show()
                }
            }
            if (child != null) {
                when(val childInstance = child.instance) {
                    is ICocktailsComponent.SlotChild.CocktailDetailsChild -> {
                        CocktailDetails(childInstance.component)
                    }
                }
            }
        },
        sheetShape = customShapeWithCutout(120f, 300f),
        // sheetPeekHeight = 600.dp,
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize()
        ) {
            Children(
                modifier = Modifier.padding(paddingValues).fillMaxSize().statusBarsPadding(),
                stack = childStack,
            ) {
                when(val child = it.instance) {
                    is ICocktailsComponent.Child.CocktailList -> {
                        CocktailListScreen(child.component)
                    }
                }
            }
            Column(modifier = Modifier.align(BottomCenter), horizontalAlignment = CenterHorizontally) {
                Button(
                    modifier = Modifier
                        .size(80.dp),
                    shape = CircleShape,
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
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
            lineTo(x = (size.width/2) - cutoutRadius, y = 0f)
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
            arcTo(
                rect = Rect(
                    offset = Offset(size.width -cornerRadius, 0f),
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