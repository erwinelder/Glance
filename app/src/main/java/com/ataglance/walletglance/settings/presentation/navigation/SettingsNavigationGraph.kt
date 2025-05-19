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
import com.ataglance.walletglance.personalization.presentation.screen.PersonalisationScreenWrapper
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.ResetDataScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreenWrapper
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreen

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<MainScreens.Settings>(
        startDestination = appConfiguration.settingsStartDestination
    ) {
        composable<SettingsScreens.Start> {
            StartSetupScreen(
                isAppThemeSetUp = appConfiguration.appTheme != null,
                onManualSetupButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Language)
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreenWrapper(
                screenPadding = scaffoldPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        authGraph(
            navController = navController,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        accountsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        budgetsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            isAppSetUp = appConfiguration.isSetUp,
        )
        categoriesGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        categoryCollectionsGraph(
            navController = navController,
            navViewModel = navViewModel
        )
        composable<SettingsScreens.Personalisation> {
            PersonalisationScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<SettingsScreens.Language> {
            LanguageScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<SettingsScreens.ResetData> {
            ResetDataScreenWrapper()
        }
    }
}