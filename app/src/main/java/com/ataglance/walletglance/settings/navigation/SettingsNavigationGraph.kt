package com.ataglance.walletglance.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.navigation.accountsGraph
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.navigation.authGraph
import com.ataglance.walletglance.budget.presentation.navigation.budgetsGraph
import com.ataglance.walletglance.category.presentation.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.navigation.categoriesGraph
import com.ataglance.walletglance.categoryCollection.presentation.navigation.categoryCollectionsGraph
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.screen.AppearanceScreen
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.settings.presentation.model.ThemeUiState
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreen
import com.ataglance.walletglance.settings.presentation.screen.ResetDataScreen
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreen
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreen
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    navigationButtonList: List<BottomBarNavigationButton>,
    authController: AuthController,
    appViewModel: AppViewModel,
    appConfiguration: AppConfiguration,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
    personalizationViewModel: PersonalizationViewModel,
    widgetNamesList: List<WidgetName>
) {
    navigation<MainScreens.Settings>(startDestination = appConfiguration.settingsStartDestination) {
        composable<SettingsScreens.Start> {
            StartSetupScreen(
                isAppThemeSetUp = appConfiguration.appTheme != null,
                onManualSetupButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Language)
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreen(
                scaffoldPadding = scaffoldPadding,
                isSignedIn = authController.isSignedIn(),
                onNavigateToScreen = { screen ->
                    navViewModel.navigateToScreen(navController, screen)
                }
            )
        }
        authGraph(
            navController = navController,
            navViewModel = navViewModel,
            authController = authController,
            appViewModel = appViewModel,
            appConfiguration = appConfiguration
        )
        accountsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            appConfiguration = appConfiguration,
            accountList = accountList
        )
        budgetsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            appViewModel = appViewModel,
            isAppSetUp = appConfiguration.isSetUp,
        )
        categoriesGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            isAppSetUp = appConfiguration.isSetUp
        )
        categoryCollectionsGraph(
            navController = navController,
            navViewModel = navViewModel
        )
        composable<SettingsScreens.Appearance> {
            AppearanceScreen(
                isAppSetUp = appConfiguration.isSetUp,
                themeUiState = themeUiState,
                onNavigateBack = navController::popBackStack,
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme,
                initialNavigationButtonList = navigationButtonList,
                onSaveNavigationButtons = navViewModel::saveBottomBarNavigationButtons,
                initialWidgetNamesList = widgetNamesList,
                onSaveWidgetNames = personalizationViewModel::saveWidgets,
                onContinueSetupButton = {
                    if (appConfiguration.isSignedIn) {
                        navViewModel.navigateToScreen(navController, SettingsScreens.Accounts)
                    } else {
                        navViewModel.navigateToSignInScreen(navController, SignInCase.Default)
                    }
                }
            )
        }
        composable<SettingsScreens.Language> {
            val viewModel = koinViewModel<LanguageViewModel> {
                parametersOf(appConfiguration.langCode)
            }

            val chosenLanguage by viewModel.langCode.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            LanguageScreen(
                isAppSetUp = appConfiguration.isSetUp,
                onNavigateBack = navController::popBackStack,
                appLanguage = appConfiguration.langCode,
                chosenLanguage = chosenLanguage,
                onSelectNewLanguage = viewModel::selectNewLanguage,
                onApplyLanguageButton = {
                    val koin = GlobalContext.get()
                    val categoriesPackageCurr = DefaultCategoriesPackage(
                        koin.get<ResourceManager> { parametersOf(appConfiguration.langCode) }
                    )
                    val categoriesPackageNew = DefaultCategoriesPackage(
                        koin.get<ResourceManager> { parametersOf(chosenLanguage) }
                    )
                    coroutineScope.launch {
                        viewModel.translatedCategories(
                            defaultInCurrLocale = categoriesPackageCurr.getAsList(),
                            defaultInNewLocale = categoriesPackageNew.getAsList()
                        )
                        viewModel.applyLanguage()
                    }
                },
                onContinueButton = {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Appearance)
                }
            )
        }
        composable<SettingsScreens.ResetData> {
            val coroutineScope = rememberCoroutineScope()

            ResetDataScreen(
                onResetData = {
                    coroutineScope.launch {
                        appViewModel.resetAppData()
                        authController.signOut()
                    }
                }
            )
        }
    }
}