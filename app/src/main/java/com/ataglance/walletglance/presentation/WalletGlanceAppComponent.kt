package com.ataglance.walletglance.presentation

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.WalletGlanceTheme
import com.ataglance.walletglance.presentation.theme.modifiers.NoRippleTheme
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel

@Composable
fun WalletGlanceAppComponent(appViewModel: AppViewModel) {
    val context = LocalContext.current as ComponentActivity
    val appUiSettings by appViewModel.appUiSettings.collectAsStateWithLifecycle()
    val themeUiState by appViewModel.themeUiState.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.safeDrawingPadding()) {
        themeUiState?.let { safeThemeUiState ->
            WalletGlanceTheme(
                context = context,
                useDeviceTheme = safeThemeUiState.useDeviceTheme,
                chosenLightTheme = safeThemeUiState.chosenLightTheme,
                chosenDarkTheme = safeThemeUiState.chosenDarkTheme,
                lastChosenTheme = safeThemeUiState.lastChosenTheme,
                setIsDarkTheme = appViewModel::updateAppThemeState,
                boxWithConstraintsScope = this
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GlanceTheme.background)
                ) {
                    AppBackground(appTheme = appUiSettings.appTheme)
                    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                        MainAppContent(
                            appViewModel = appViewModel,
                            appUiSettings = appUiSettings,
                            themeUiState = safeThemeUiState
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBackground(appTheme: AppTheme?) {
    AnimatedContent(
        targetState = appTheme,
        label = "App background",
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { targetAppTheme ->
        targetAppTheme?.let {
            val imageAndDescription = when (it) {
                AppTheme.LightDefault -> R.drawable.main_background_light to
                        "application light background"
                AppTheme.DarkDefault -> R.drawable.main_background_dark to
                        "application dark background"
            }
            Image(
                painter = painterResource(imageAndDescription.first),
                contentDescription = imageAndDescription.second,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        } ?: Box(modifier = Modifier.fillMaxSize())
    }
}