package com.ataglance.walletglance.core.presentation.components

import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WalletGlanceTheme
import com.ataglance.walletglance.core.presentation.components.other.AppBackground
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GlanceAppComponent(
    authController: AuthController,
    billingSubscriptionManager: BillingSubscriptionManager,
    appViewModel: AppViewModel,
    navViewModel: NavigationViewModel,
    navController: NavHostController,
    personalizationViewModel: PersonalizationViewModel
) {
    val context = LocalContext.current as ComponentActivity
    val appUiSettings by appViewModel.appConfiguration.collectAsStateWithLifecycle()
    val themeUiState by appViewModel.themeUiState.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.safeDrawingPadding()) {
        SharedTransitionLayout {
            themeUiState?.let { safeThemeUiState ->
                WalletGlanceTheme(
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
                            .background(GlanceTheme.background)
                    ) {
                        AppBackground(appTheme = appUiSettings.appTheme)
                        CompositionLocalProvider(LocalRippleConfiguration provides null) {
                            MainAppContent(
                                authController = authController,
                                billingSubscriptionManager = billingSubscriptionManager,
                                appViewModel = appViewModel,
                                appConfiguration = appUiSettings,
                                themeUiState = safeThemeUiState,
                                navViewModel = navViewModel,
                                navController = navController,
                                personalizationViewModel = personalizationViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}