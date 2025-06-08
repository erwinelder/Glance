package com.ataglance.walletglance.core.presentation.theme

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.WindowType


private val LocalAppTheme = compositionLocalOf { AppTheme.LightDefault }
private val LocalColors = staticCompositionLocalOf<GlanciPalette> { GlanciPalette.LightDefault }
private val LocalTypography = staticCompositionLocalOf { GlanciCustomTypography() }
private val LocalWindowType = staticCompositionLocalOf { WindowType.Compact }
@OptIn(ExperimentalSharedTransitionApi::class)
private val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GlanciTheme(
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

    val glanciColors = when (appTheme) {
        AppTheme.LightDefault -> GlanciPalette.LightDefault
        AppTheme.DarkDefault -> GlanciPalette.DarkDefault
    }
    val typography = GlanciCustomTypography()
    val windowType = when {
        boxWithConstraintsScope.maxWidth < 600.dp -> WindowType.Compact
        boxWithConstraintsScope.maxWidth < 840.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }

    CompositionLocalProvider(
        LocalAppTheme provides appTheme,
        LocalColors provides glanciColors,
        LocalTypography provides typography,
        LocalWindowType provides windowType,
        LocalSharedTransitionScope provides sharedTransitionScope,
        LocalRippleConfiguration provides null
    ) {
        MaterialTheme(content = content)
    }
}


val CurrAppTheme: AppTheme
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTheme.current

val GlanciColors: GlanciPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

val GlanciTypography: GlanciCustomTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

val CurrWindowType: WindowType
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowType.current

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
