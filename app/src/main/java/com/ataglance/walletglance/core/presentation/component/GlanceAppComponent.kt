package com.ataglance.walletglance.core.presentation.component

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.core.presentation.component.other.AppBackground
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.GlanceTheme
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GlanceAppComponent(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    val context = LocalActivity.current as ComponentActivity
    val appThemeConfiguration by appViewModel.appThemeConfiguration.collectAsStateWithLifecycle()
    val appConfiguration by appViewModel.appConfiguration.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.safeDrawingPadding()) {
        SharedTransitionLayout {
            appThemeConfiguration?.let { themeConfiguration ->
                GlanceTheme(
                    context = context,
                    useDeviceTheme = themeConfiguration.useDeviceTheme,
                    chosenLightTheme = themeConfiguration.chosenLightTheme,
                    chosenDarkTheme = themeConfiguration.chosenDarkTheme,
                    lastChosenTheme = themeConfiguration.lastChosenTheme,
                    setAppTheme = appViewModel::setAppTheme,
                    boxWithConstraintsScope = this@BoxWithConstraints,
                    sharedTransitionScope = this@SharedTransitionLayout
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GlanceColors.background)
                    ) {
                        AppBackground(appTheme = appConfiguration.appTheme)
                        MainAppContent(
                            appConfiguration = appConfiguration,
                            navController = navController,
                            appViewModel = appViewModel
                        )
                    }
                }
            }
        }
    }
}