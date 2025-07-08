package com.ataglance.walletglance.category.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.category.domain.navigation.CategoriesSettingsScreens
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreenWrapper
import com.ataglance.walletglance.category.presentation.screen.EditCategoryScreenWrapper
import com.ataglance.walletglance.category.presentation.screen.EditSubcategoriesScreenWrapper
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

fun NavGraphBuilder.categoriesGraph(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Categories>(
        startDestination = CategoriesSettingsScreens.EditCategories
    ) {
        composable<CategoriesSettingsScreens.EditCategories> { backStack ->
            EditCategoriesScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<CategoriesSettingsScreens.EditSubcategories> { backStack ->
            EditSubcategoriesScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<CategoriesSettingsScreens.EditCategory> { backStack ->
            EditCategoryScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                appConfiguration = appConfiguration
            )
        }
    }
}