package com.cocktailbar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cocktailbar.R

private val DidactGothicRegularFontFamily = FontFamily(
        Font(resId = R.font.didact_gothic_regular, weight = FontWeight.Normal),
        Font(resId = R.font.didact_gothic_regular, weight = FontWeight.Normal),
        Font(resId = R.font.didact_gothic_regular, weight = FontWeight.Normal)
)
// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily = DidactGothicRegularFontFamily,
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                letterSpacing = 0.5.sp,
                platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                ),
        ),
        bodyMedium = TextStyle(
                fontFamily = DidactGothicRegularFontFamily,
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
                platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                ),
        ),
        headlineLarge = TextStyle(
                fontFamily = DidactGothicRegularFontFamily,
                fontWeight = FontWeight(400),
                fontSize = 36.sp,
                lineHeight = 47.sp,
                letterSpacing = 0.5.sp,
                platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                ),
        ),
        headlineMedium = TextStyle(
                fontFamily = DidactGothicRegularFontFamily,
                fontWeight = FontWeight(400),
                fontSize = 32.sp,
                lineHeight = 38.4.sp,
                letterSpacing = 0.5.sp,
                platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                ),
        ),
        headlineSmall = TextStyle(
                fontFamily = DidactGothicRegularFontFamily,
                fontWeight = FontWeight(400),
                fontSize = 24.sp,
                lineHeight = 31.sp,
                letterSpacing = 0.5.sp,
                platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                ),
        ),
        /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)