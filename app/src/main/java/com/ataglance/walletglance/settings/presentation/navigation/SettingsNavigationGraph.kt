package com.ataglance.walletglance.settings.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.account.presentation.navigation.accountsGraph
import com.ataglance.walletglance.auth.presentation.navigation.authGraph
import com.ataglance.walletglance.budget.presentation.navigation.budgetsGraph
import com.ataglance.walletglance.category.presentation.navigation.categoriesGraph
import com.ataglance.walletglance.categoryCollection.presentation.navigation.categoryCollectionsGraph
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.notification.presentation.screen.NotificationsScreenWrapper
import com.ataglance.walletglance.personalization.presentation.screen.PersonalizationScreenWrapper
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.ResetDataScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreen

fun NavGraphBuilder.settingsGraph(
    screenPadding: PaddingValues,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<MainScreens.Settings>(
        startDestination = appConfiguration.settingsStartDestination
    ) {
        composable<SettingsScreens.Start> {
            StartSetupScreen(
                screenPadding = screenPadding,
                isAppThemeSetUp = appConfiguration.appTheme != null,
                onManualSetupButton = {
                    navViewModel.navigateToScreen(
                        navController = navController, screen = SettingsScreens.Language
                    )
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        authGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        accountsGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        budgetsGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel,
            isAppSetUp = appConfiguration.isSetUp
        )
        categoriesGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        categoryCollectionsGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel
        )
        composable<SettingsScreens.Personalisation> {
            PersonalizationScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<SettingsScreens.Notifications> {
            NotificationsScreenWrapper(
                screenPadding = screenPadding,
                navController = navController
            )
        }
        composable<SettingsScreens.Language> {
            LanguageScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<SettingsScreens.ResetData> {
            ResetDataScreenWrapper(
                screenPadding = screenPadding,
                navController = navController
            )
        }
    }
}