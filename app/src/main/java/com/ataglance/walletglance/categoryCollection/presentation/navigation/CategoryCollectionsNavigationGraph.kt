package com.ataglance.walletglance.categoryCollection.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.categoryCollection.domain.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreenWrapper
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionsScreenWrapper
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

fun NavGraphBuilder.categoryCollectionsGraph(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    navigation<SettingsScreens.CategoryCollections>(
        startDestination = CategoryCollectionsSettingsScreens.EditCategoryCollections
    ) {
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollections> { backStack ->
            EditCategoryCollectionsScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollection> { backStack ->
            EditCategoryCollectionScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController
            )
        }
    }
}