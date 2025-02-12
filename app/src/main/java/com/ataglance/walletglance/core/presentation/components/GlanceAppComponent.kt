package com.ataglance.walletglance.core.presentation.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.other.AppBackground
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GlanceAppComponent(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    personalizationViewModel: PersonalizationViewModel
) {
    val context = LocalActivity.current as ComponentActivity
    val appConfiguration by appViewModel.appConfiguration.collectAsStateWithLifecycle()
    val themeUiState by appViewModel.themeUiState.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.safeDrawingPadding()) {
        SharedTransitionLayout {
            themeUiState?.let { safeThemeUiState ->
                GlanceTheme(
                    context = context,
                    useDeviceTheme = safeThemeUiState.useDeviceTheme,
                    chosenLightTheme = safeThemeUiState.chosenLightTheme,
                    chosenDarkTheme = safeThemeUiState.chosenDarkTheme,
                    lastChosenTheme = safeThemeUiState.lastChosenTheme,
                    setIsDarkTheme = appViewModel::updateAppThemeState,
                    boxWithConstraintsScope = this@BoxWithConstraints,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GlanceColors.background)
                    ) {
                        AppBackground(appTheme = appConfiguration.appTheme)
                        CompositionLocalProvider(LocalRippleConfiguration provides null) {
                            MainAppContent(
                                appConfiguration = appConfiguration,
                                themeUiState = safeThemeUiState,
                                navController = navController,
                                navViewModel = navViewModel,
                                appViewModel = appViewModel,
                                personalizationViewModel = personalizationViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}