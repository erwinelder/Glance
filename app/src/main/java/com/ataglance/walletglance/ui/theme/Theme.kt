package com.ataglance.walletglance.ui.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.app.WindowType
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.color.GlanceColors

private val LightDefaultPalette = GlanceColors(
    material = lightColorScheme(
        primary = md_theme_light_default_primary,
        onPrimary = md_theme_light_default_onPrimary,
        primaryContainer = md_theme_light_default_primaryContainer,
        onPrimaryContainer = md_theme_light_default_onPrimaryContainer,
        secondary = md_theme_light_default_secondary,
        onSecondary = md_theme_light_default_onSecondary,
        secondaryContainer = md_theme_light_default_secondaryContainer,
        onSecondaryContainer = md_theme_light_default_onSecondaryContainer,
        tertiary = md_theme_light_default_tertiary,
        onTertiary = md_theme_light_default_onTertiary,
        tertiaryContainer = md_theme_light_default_tertiaryContainer,
        onTertiaryContainer = md_theme_light_default_onTertiaryContainer,
        error = md_theme_light_default_error,
        errorContainer = md_theme_light_default_errorContainer,
        onError = md_theme_light_default_onError,
        onErrorContainer = md_theme_light_default_onErrorContainer,
        background = md_theme_light_default_background,
        onBackground = md_theme_light_default_onBackground,
        surface = md_theme_light_default_surface,
        onSurface = md_theme_light_default_onSurface,
        surfaceVariant = md_theme_light_default_surfaceVariant,
        onSurfaceVariant = md_theme_light_default_onSurfaceVariant,
        outline = md_theme_light_default_outline,
        inverseOnSurface = md_theme_light_default_inverseOnSurface,
        inverseSurface = md_theme_light_default_inverseSurface,
        inversePrimary = md_theme_light_default_inversePrimary,
        surfaceTint = md_theme_light_default_surfaceTint,
        outlineVariant = md_theme_light_default_outlineVariant,
        scrim = md_theme_light_default_scrim,
    ),
    primaryGradientLightToDark = md_theme_light_default_primaryGradientLightToDark,
    glassSurfaceGradient = md_theme_light_default_glassSurfaceGradient,
    glassSurfaceBorder = md_theme_light_default_glassSurfaceBorder,
    glassSurfaceLightAndDarkShadow = md_theme_light_default_glassSurfaceLightAndDarkShadow,
    onGlassSurfaceGradient = md_theme_light_default_onGlassSurfaceGradient,
    onGlassSurfaceBorder = md_theme_light_default_onGlassSurfaceBorder,
    glassGradientLightToDark = md_theme_light_default_glassGradientLightToDark,
    disabledGradientLightToDark = md_theme_light_default_disabledGradientLightToDark,
    errorGradientLightToDark = md_theme_light_default_errorGradientLightToDark,
    greenGradientPaleToSaturated = md_theme_light_default_green,
    redGradientPaleToSaturated = md_theme_light_default_red
)

private val DarkDefaultPalette = GlanceColors(
    material = darkColorScheme(
        primary = md_theme_dark_default_primary,
        onPrimary = md_theme_dark_default_onPrimary,
        primaryContainer = md_theme_dark_default_primaryContainer,
        onPrimaryContainer = md_theme_dark_default_onPrimaryContainer,
        secondary = md_theme_dark_default_secondary,
        onSecondary = md_theme_dark_default_onSecondary,
        secondaryContainer = md_theme_dark_default_secondaryContainer,
        onSecondaryContainer = md_theme_dark_default_onSecondaryContainer,
        tertiary = md_theme_dark_default_tertiary,
        onTertiary = md_theme_dark_default_onTertiary,
        tertiaryContainer = md_theme_dark_default_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_default_onTertiaryContainer,
        error = md_theme_dark_default_error,
        errorContainer = md_theme_dark_default_errorContainer,
        onError = md_theme_dark_default_onError,
        onErrorContainer = md_theme_dark_default_onErrorContainer,
        background = md_theme_dark_default_background,
        onBackground = md_theme_dark_default_onBackground,
        surface = md_theme_dark_default_surface,
        onSurface = md_theme_dark_default_onSurface,
        surfaceVariant = md_theme_dark_default_surfaceVariant,
        onSurfaceVariant = md_theme_dark_default_onSurfaceVariant,
        outline = md_theme_dark_default_outline,
        inverseOnSurface = md_theme_dark_default_inverseOnSurface,
        inverseSurface = md_theme_dark_default_inverseSurface,
        inversePrimary = md_theme_dark_default_inversePrimary,
        surfaceTint = md_theme_dark_default_surfaceTint,
        outlineVariant = md_theme_dark_default_outlineVariant,
        scrim = md_theme_dark_default_scrim,
    ),
    primaryGradientLightToDark = md_theme_dark_default_primaryGradientLightToDark,
    glassSurfaceGradient = md_theme_dark_default_glassSurfaceGradient,
    glassSurfaceBorder = md_theme_dark_default_glassSurfaceBorder,
    glassSurfaceLightAndDarkShadow = md_theme_dark_default_glassSurfaceLightAndDarkShadow,
    onGlassSurfaceGradient = md_theme_dark_default_onGlassSurfaceGradient,
    onGlassSurfaceBorder = md_theme_dark_default_onGlassSurfaceBorder,
    glassGradientLightToDark = md_theme_dark_default_glassGradientLightToDark,
    disabledGradientLightToDark = md_theme_dark_default_disabledGradientLightToDark,
    errorGradientLightToDark = md_theme_dark_default_errorGradientLightToDark,
    greenGradientPaleToSaturated = md_theme_dark_default_green,
    redGradientPaleToSaturated = md_theme_dark_default_red
)

private val DarkBluePalette = GlanceColors(
    material = darkColorScheme(
        primary = md_theme_dark_blue_primary,
        onPrimary = md_theme_dark_blue_onPrimary,
        primaryContainer = md_theme_dark_blue_primaryContainer,
        onPrimaryContainer = md_theme_dark_blue_onPrimaryContainer,
        secondary = md_theme_dark_blue_secondary,
        onSecondary = md_theme_dark_blue_onSecondary,
        secondaryContainer = md_theme_dark_blue_secondaryContainer,
        onSecondaryContainer = md_theme_dark_blue_onSecondaryContainer,
        tertiary = md_theme_dark_blue_tertiary,
        onTertiary = md_theme_dark_blue_onTertiary,
        tertiaryContainer = md_theme_dark_blue_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_blue_onTertiaryContainer,
        error = md_theme_dark_blue_error,
        errorContainer = md_theme_dark_blue_errorContainer,
        onError = md_theme_dark_blue_onError,
        onErrorContainer = md_theme_dark_blue_onErrorContainer,
        background = md_theme_dark_blue_background,
        onBackground = md_theme_dark_blue_onBackground,
        surface = md_theme_dark_blue_surface,
        onSurface = md_theme_dark_blue_onSurface,
        surfaceVariant = md_theme_dark_blue_surfaceVariant,
        onSurfaceVariant = md_theme_dark_blue_onSurfaceVariant,
        outline = md_theme_dark_blue_outline,
        inverseOnSurface = md_theme_dark_blue_inverseOnSurface,
        inverseSurface = md_theme_dark_blue_inverseSurface,
        inversePrimary = md_theme_dark_blue_inversePrimary,
        surfaceTint = md_theme_dark_blue_surfaceTint,
        outlineVariant = md_theme_dark_blue_outlineVariant,
        scrim = md_theme_dark_blue_scrim,
    ),
    primaryGradientLightToDark = md_theme_dark_blue_primaryGradientLightToDark,
    glassSurfaceGradient = md_theme_dark_blue_glassSurfaceGradient,
    glassSurfaceBorder = md_theme_dark_blue_glassSurfaceBorder,
    glassSurfaceLightAndDarkShadow = md_theme_dark_blue_glassSurfaceLightAndDarkShadow,
    onGlassSurfaceGradient = md_theme_dark_blue_onGlassSurfaceGradient,
    onGlassSurfaceBorder = md_theme_dark_blue_onGlassSurfaceBorder,
    glassGradientLightToDark = md_theme_dark_blue_glassGradientLightToDark,
    disabledGradientLightToDark = md_theme_dark_blue_disabledGradientLightToDark,
    errorGradientLightToDark = md_theme_dark_blue_errorGradientLightToDark,
    greenGradientPaleToSaturated = md_theme_dark_blue_green,
    redGradientPaleToSaturated = md_theme_dark_blue_red
)

private val LocalColors = staticCompositionLocalOf { LightDefaultPalette }
private val LocalWindowType = staticCompositionLocalOf { WindowType.Compact }

@Composable
fun WalletGlanceTheme(
    context: ComponentActivity? = null,
    useDeviceTheme: Boolean = true,
    chosenLightTheme: String = AppTheme.LightDefault.name,
    chosenDarkTheme: String = AppTheme.DarkDefault.name,
    lastChosenTheme: String = AppTheme.LightDefault.name,
    isDeviceIsDarkTheme: Boolean = isSystemInDarkTheme(),
    setIsDarkTheme: (String) -> Unit = {},
    boxWithConstraintsScope: BoxWithConstraintsScope,
    content: @Composable () -> Unit
) {
    val appTheme = if (useDeviceTheme) {
        if (!isDeviceIsDarkTheme) {
            chosenLightTheme
        } else {
            chosenDarkTheme
        }
    } else {
        lastChosenTheme
    }

    setIsDarkTheme(appTheme)

    val colorScheme = when (appTheme) {
        AppTheme.LightDefault.name -> LightDefaultPalette
        AppTheme.DarkDefault.name -> DarkDefaultPalette
        else -> DarkBluePalette
    }
    val windowType = when {
        boxWithConstraintsScope.maxWidth < 600.dp -> WindowType.Compact
        boxWithConstraintsScope.maxWidth < 840.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }

    context?.let {
        LaunchedEffect(key1 = isDeviceIsDarkTheme, key2 = lastChosenTheme, key3 = useDeviceTheme) {
            setSystemBarsColors(it, colorScheme, appTheme)
        }
    }

    CompositionLocalProvider(
        LocalColors provides colorScheme,
        LocalWindowType provides windowType
    ) {
        MaterialTheme(
            colorScheme = colorScheme.material,
            typography = Typography,
            content = content,
        )
    }
}

private fun setSystemBarsColors(
    context: ComponentActivity,
    colorScheme: GlanceColors,
    appTheme: String
) {
    context.enableEdgeToEdge(
        statusBarStyle = when (appTheme) {
            AppTheme.LightDefault.name ->
                SystemBarStyle.light(
                    colorScheme.material.surface.toArgb(),
                    colorScheme.material.background.toArgb()
                )
            AppTheme.DarkDefault.name ->
                SystemBarStyle.dark(colorScheme.material.surface.toArgb())
            else -> SystemBarStyle.dark(colorScheme.material.surface.toArgb())
        },
        navigationBarStyle = when (appTheme) {
            AppTheme.LightDefault.name ->
                SystemBarStyle.light(
                    colorScheme.material.surface.toArgb(),
                    colorScheme.material.background.toArgb()
                )
            AppTheme.DarkDefault.name ->
                SystemBarStyle.dark(colorScheme.material.surface.toArgb())
            else -> SystemBarStyle.dark(colorScheme.material.surface.toArgb())
        }
    )
}

val GlanceTheme: GlanceColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

val WindowTypeIsCompact: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowType.current == WindowType.Compact

val WindowTypeIsMedium: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowType.current == WindowType.Medium

val WindowTypeIsExpanded: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowType.current == WindowType.Expanded
