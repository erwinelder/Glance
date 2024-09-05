package com.ataglance.walletglance.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.navigation.accountsGraph
import com.ataglance.walletglance.budget.domain.BudgetsByType
import com.ataglance.walletglance.budget.navigation.budgetsGraph
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.navigation.categoriesGraph
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.navigation.categoryCollectionsGraph
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.screen.AppearanceScreen
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreen
import com.ataglance.walletglance.settings.presentation.screen.SettingsDataScreen
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreen
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreen
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModelFactory
import kotlinx.coroutines.launch

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIdsByType,
    budgetsByType: BudgetsByType
) {
    navigation<MainScreens.Settings>(startDestination = appUiSettings.startSettingsDestination) {
        composable<SettingsScreens.Start> {
            StartSetupScreen(
                isAppThemeSetUp = appUiSettings.appTheme != null,
                onManualSetupButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Language)
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreen(
                scaffoldPadding = scaffoldPadding,
                onNavigateToScreen = { screen ->
                    navViewModel.navigateToScreen(navController, screen)
                }
            )
        }
        accountsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            accountList = accountList
        )
        budgetsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            budgetsByType = budgetsByType,
            accountList = accountList,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
        categoriesGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            isAppSetUp = appUiSettings.isSetUp,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
        categoryCollectionsGraph(
            navController = navController,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            categoriesWithSubcategories = categoriesWithSubcategories,
            categoryCollectionsWithIdsByType = categoryCollectionsUiState
        )
        composable<SettingsScreens.Appearance> {
            AppearanceScreen(
                isAppSetUp = appUiSettings.isSetUp,
                themeUiState = themeUiState,
                onContinueSetupButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Accounts)
                },
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme
            )
        }
        composable<SettingsScreens.Language> {
            val viewModel = viewModel<LanguageViewModel>(
                factory = LanguageViewModelFactory(appUiSettings.langCode)
            )

            val chosenLanguage by viewModel.langCode.collectAsState()
            val context = LocalContext.current

            LanguageScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appLanguage = appUiSettings.langCode,
                chosenLanguage = chosenLanguage,
                onSelectNewLanguage = viewModel::selectNewLanguage,
                onApplyLanguageButton = { langCode: String ->
                    appViewModel.translateAndSaveCategoriesWithDefaultNames(
                        currentLangCode = appUiSettings.langCode,
                        newLangCode = langCode,
                        context = context
                    )
                    appViewModel.setLanguage(langCode)
                },
                onContinueButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Appearance)
                }
            )
        }
        composable<SettingsScreens.ResetData> {
            val coroutineScope = rememberCoroutineScope()

            SettingsDataScreen(
                onResetData = {
                    coroutineScope.launch {
                        appViewModel.resetAppData()
                    }
                }
            )
        }
    }
}