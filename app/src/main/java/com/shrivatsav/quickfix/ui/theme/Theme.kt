package com.shrivatsav.quickfix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimaryLight,
    onPrimary = BrandOnPrimaryLight,
    primaryContainer = BrandPrimaryContainerLight,
    onPrimaryContainer = BrandOnPrimaryContainerLight,
    secondary = BrandSecondaryLight,
    onSecondary = BrandOnSecondaryLight,
    secondaryContainer = BrandSecondaryContainerLight,
    onSecondaryContainer = BrandOnSecondaryContainerLight,
    tertiary = BrandTertiaryLight,
    onTertiary = BrandOnTertiaryLight,
    tertiaryContainer = BrandTertiaryContainerLight,
    onTertiaryContainer = BrandOnTertiaryContainerLight,
    error = BrandErrorLight,
    onError = BrandOnErrorLight,
    errorContainer = BrandErrorContainerLight,
    onErrorContainer = BrandOnErrorContainerLight,
    background = BrandBackgroundLight,
    surface = BrandSurfaceLight,
    surfaceContainer = BrandSurfaceContainerLight,
    surfaceContainerLow = BrandSurfaceContainerLowLight,
    surfaceContainerHigh = BrandSurfaceContainerHighLight,
    outline = BrandOutlineLight,
    outlineVariant = BrandOutlineVariantLight,
    onSurface = BrandOnSurfaceLight,
    onSurfaceVariant = BrandOnSurfaceVariantLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = BrandOnPrimaryDark,
    primaryContainer = BrandPrimaryContainerDark,
    onPrimaryContainer = BrandOnPrimaryContainerDark,
    secondary = BrandSecondaryDark,
    onSecondary = BrandOnSecondaryDark,
    secondaryContainer = BrandSecondaryContainerDark,
    onSecondaryContainer = BrandOnSecondaryContainerDark,
    tertiary = BrandTertiaryDark,
    onTertiary = BrandOnTertiaryDark,
    tertiaryContainer = BrandTertiaryContainerDark,
    onTertiaryContainer = BrandOnTertiaryContainerDark,
    error = BrandErrorDark,
    onError = BrandOnErrorDark,
    errorContainer = BrandErrorContainerDark,
    onErrorContainer = BrandOnErrorContainerDark,
    background = BrandBackgroundDark,
    surface = BrandSurfaceDark,
    surfaceContainer = BrandSurfaceContainerDark,
    surfaceContainerLow = BrandSurfaceContainerLowDark,
    surfaceContainerHigh = BrandSurfaceContainerHighDark,
    outline = BrandOutlineDark,
    outlineVariant = BrandOutlineVariantDark,
    onSurface = BrandOnSurfaceDark,
    onSurfaceVariant = BrandOnSurfaceVariantDark,
)

private val QuickFixShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(30.dp),
    extraLarge = RoundedCornerShape(999.dp),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuickFixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = QuickFixShapes,
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}