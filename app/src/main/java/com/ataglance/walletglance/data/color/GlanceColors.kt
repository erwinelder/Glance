package com.ataglance.walletglance.data.color

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

data class GlanceColors(
    val material: ColorScheme,
    val primaryGradientLightToDark: Pair<Color, Color>,
    val glassSurfaceGradient: List<Color>,
    val glassSurfaceBorder: Color,
    val glassSurfaceLightAndDarkShadow: Pair<Color, Color>,
    val onGlassSurfaceGradient: List<Color>,
    val onGlassSurfaceBorder: Color,
    val glassGradientLightToDark: Pair<Color, Color>,
    val surfaceGradient: List<Color>,
    val disabledGradientLightToDark: Pair<Color, Color>,
    val errorGradientLightToDark: Pair<Color, Color>,
    val greenGradientPaleToSaturated: Pair<Color, Color>,
    val redGradientPaleToSaturated: Pair<Color, Color>,
    val greenGradient: Pair<Color, Color>,
    val yellowGradient: Pair<Color, Color>,
    val redGradient: Pair<Color, Color>
) {
    val primary: Color get() = material.primary
    val onPrimary: Color get() = material.onPrimary
    val primaryContainer: Color get() = material.primaryContainer
    val onPrimaryContainer: Color get() = material.onPrimaryContainer
    val secondary: Color get() = material.secondary
    val onSecondary: Color get() = material.onSecondary
    val secondaryContainer: Color get() = material.secondaryContainer
    val onSecondaryContainer: Color get() = material.onSecondaryContainer
    val tertiary: Color get() = material.tertiary
    val onTertiary: Color get() = material.onTertiary
    val tertiaryContainer: Color get() = material.tertiaryContainer
    val onTertiaryContainer: Color get() = material.onTertiaryContainer
    val error: Color get() = material.error
    val errorContainer: Color get() = material.errorContainer
    val onError: Color get() = material.onError
    val onErrorContainer: Color get() = material.onErrorContainer
    val background: Color get() = material.background
    val onBackground: Color get() = material.onBackground
    val surface: Color get() = material.surface
    val onSurface: Color get() = material.onSurface
    val surfaceVariant: Color get() = material.surfaceVariant
    val onSurfaceVariant: Color get() = material.onSurfaceVariant
    val outline: Color get() = material.outline
    val inverseOnSurface: Color get() = material.inverseOnSurface
    val inverseSurface: Color get() = material.inverseSurface
    val inversePrimary: Color get() = material.inversePrimary
    val surfaceTint: Color get() = material.surfaceTint
    val outlineVariant: Color get() = material.outlineVariant
    val scrim: Color get() = material.scrim
}
