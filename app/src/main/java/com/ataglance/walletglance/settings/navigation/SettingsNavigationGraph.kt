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
import com.ataglance.walletglance.account.presentation.navigation.accountsGraph
import com.ataglance.walletglance.auth.domain.AuthController
import com.ataglance.walletglance.auth.presentation.navigation.authGraph
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.navigation.budgetsGraph
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.presentation.navigation.categoriesGraph
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.presentation.navigation.categoryCollectionsGraph
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.screen.AppearanceScreen
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.settings.domain.ThemeUiState
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
    navigationButtonList: List<BottomBarNavigationButton>,
    authController: AuthController,
    subscriptionViewModel: SubscriptionViewModel,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIdsByType,
    budgetsByType: BudgetsByType,
    personalizationViewModel: PersonalizationViewModel,
    widgetNamesList: List<WidgetName>
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
        authGraph(
            navController = navController,
            navViewModel = navViewModel,
            authController = authController,
            subscriptionViewModel = subscriptionViewModel,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings
        )
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
            isAppSetUp = appUiSettings.isSetUp,
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
                onNavigateBack = navController::popBackStack,
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme,
                initialNavigationButtonList = navigationButtonList,
                onSaveNavigationButtons = navViewModel::saveBottomBarNavigationButtons,
                initialWidgetNamesList = widgetNamesList,
                onSaveWidgetNames = personalizationViewModel::saveWidgetList,
                onContinueSetupButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Accounts)
                }
            )
        }
        composable<SettingsScreens.Language> {
            val viewModel = viewModel<LanguageViewModel>(
                factory = LanguageViewModelFactory(appUiSettings.langCode)
            )

            val chosenLanguage by viewModel.langCode.collectAsState()
            val context = LocalContext.current

            LanguageScreen(
                isAppSetUp = appUiSettings.isSetUp,
                onNavigateBack = navController::popBackStack,
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