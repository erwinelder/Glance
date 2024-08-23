package com.ataglance.walletglance.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.settings.ThemeUiState
import com.ataglance.walletglance.presentation.ui.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.ui.screens.settings.SettingsDataScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.SettingsHomeScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.SetupAppearanceScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.SetupLanguageScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.SetupStartScreen
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel
import com.ataglance.walletglance.presentation.viewmodels.settings.LanguageViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIds,
    budgetsByType: BudgetsByType
) {
    navigation<MainScreens.Settings>(startDestination = appUiSettings.startSettingsDestination) {
        composable<SettingsScreens.Start> {
            SetupStartScreen(
                appTheme = appUiSettings.appTheme,
                onManualSetupButton = {
                    navController.navigate(SettingsScreens.Language)
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                onNavigateToScreen = { screen: SettingsScreens ->
                    navController.navigate(screen) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<SettingsScreens.Language> {
            val viewModel = viewModel<LanguageViewModel>()
            val chosenLanguage by viewModel.langCode.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(true) {
                if (chosenLanguage == null) {
                    viewModel.chooseNewLanguage(appUiSettings.langCode)
                }
            }

            SetupLanguageScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appLanguage = appUiSettings.langCode,
                chosenLanguage = chosenLanguage,
                chooseNewLanguage = viewModel::chooseNewLanguage,
                onApplyLanguageButton = { langCode: String ->
                    appViewModel.translateAndSaveCategoriesWithDefaultNames(
                        currentLangCode = appUiSettings.langCode,
                        newLangCode = langCode,
                        context = context
                    )
                    appViewModel.setLanguage(langCode)
                },
                onContinueButton = {
                    navController.navigate(SettingsScreens.Appearance)
                }
            )
        }
        composable<SettingsScreens.Appearance> {
            SetupAppearanceScreen(
                isAppSetUp = appUiSettings.isSetUp,
                themeUiState = themeUiState,
                onContinueSetupButton = {
                    navController.navigate(SettingsScreens.Accounts)
                },
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme
            )
        }
        accountsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            accountList = accountList
        )
        budgetsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            budgetsByType = budgetsByType,
            accountList = accountList,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
        categoriesGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
        categoryCollectionsGraph(
            navController = navController,
            appViewModel = appViewModel,
            appTheme = appUiSettings.appTheme,
            categoriesWithSubcategories = categoriesWithSubcategories,
            categoryCollectionsWithIds = categoryCollectionsUiState
        )
        composable<SettingsScreens.ResetData> {
            val coroutineScope = rememberCoroutineScope()

            SettingsDataScreen(
                onResetData = {
                    coroutineScope.launch {
                        appViewModel.resetAppData()
                    }
                },
                onExportData = {}
            )
        }
    }
}