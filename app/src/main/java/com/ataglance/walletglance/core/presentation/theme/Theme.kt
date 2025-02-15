package com.ataglance.walletglance.core.presentation.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.WindowType

private val LocalColors = staticCompositionLocalOf<GlancePalette> { GlancePalette.LightDefault }
val LocalWindowType = staticCompositionLocalOf { WindowType.Compact }
private val LocalAppTheme = compositionLocalOf { AppTheme.LightDefault }
@OptIn(ExperimentalSharedTransitionApi::class)
private val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GlanceTheme(
    context: ComponentActivity? = null,
    useDeviceTheme: Boolean = true,
    chosenLightTheme: AppTheme = AppTheme.LightDefault,
    chosenDarkTheme: AppTheme = AppTheme.DarkDefault,
    lastChosenTheme: AppTheme = AppTheme.LightDefault,
    isDeviceIsDarkTheme: Boolean = isSystemInDarkTheme(),
    setAppTheme: (AppTheme) -> Unit = {},
    boxWithConstraintsScope: BoxWithConstraintsScope,
    sharedTransitionScope: SharedTransitionScope,
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

    setAppTheme(appTheme)

    val glanceColors = when (appTheme) {
        AppTheme.LightDefault -> GlancePalette.LightDefault
        AppTheme.DarkDefault -> GlancePalette.DarkDefault
    }
    val windowType = when {
        boxWithConstraintsScope.maxWidth < 600.dp -> WindowType.Compact
        boxWithConstraintsScope.maxWidth < 840.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }

    context?.let {
        LaunchedEffect(key1 = isDeviceIsDarkTheme, key2 = lastChosenTheme, key3 = useDeviceTheme) {
            setSystemBarsColors(it, glanceColors, appTheme)
        }
    }

    CompositionLocalProvider(
        LocalColors provides glanceColors,
        LocalWindowType provides windowType,
        LocalAppTheme provides appTheme,
        LocalSharedTransitionScope provides sharedTransitionScope,
        LocalRippleConfiguration provides null
    ) {
        MaterialTheme(typography = Typography, content = content)
    }
}

private fun setSystemBarsColors(
    context: ComponentActivity,
    colorScheme: GlancePalette,
    appTheme: AppTheme
) {
    context.enableEdgeToEdge(
        statusBarStyle = when (appTheme) {
            AppTheme.LightDefault ->
                SystemBarStyle.light(
                    colorScheme.surface.toArgb(),
                    colorScheme.background.toArgb()
                )
            AppTheme.DarkDefault ->
                SystemBarStyle.dark(colorScheme.surface.toArgb())
        },
        navigationBarStyle = when (appTheme) {
            AppTheme.LightDefault ->
                SystemBarStyle.light(
                    colorScheme.surface.toArgb(),
                    colorScheme.background.toArgb()
                )
            AppTheme.DarkDefault -> SystemBarStyle.dark(colorScheme.surface.toArgb())
        }
    )
}


val GlanceColors: GlancePalette
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

val CurrAppTheme: AppTheme
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTheme.current

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

@OptIn(ExperimentalSharedTransitionApi::class)
val CurrSharedTransitionScope: SharedTransitionScope
    @Composable
    @ReadOnlyComposable
    get() = LocalSharedTransitionScope.current!!
