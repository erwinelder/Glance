package com.ataglance.walletglance.categoryCollection.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.categoryCollection.domain.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreen
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionsScreen
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionsViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoryCollectionsGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    navigation<SettingsScreens.CategoryCollections>(
        startDestination = CategoryCollectionsSettingsScreens.EditCategoryCollections
    ) {
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollections> { backStack ->
            val collectionsViewModel = backStack
                .sharedKoinNavViewModel<EditCategoryCollectionsViewModel>(navController)
            val collectionViewModel = backStack
                .sharedKoinNavViewModel<EditCategoryCollectionViewModel>(navController)

            val collectionsByType by collectionsViewModel.collectionsByType.collectAsStateWithLifecycle()
            val collectionType by collectionsViewModel.collectionType.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditCategoryCollectionsScreen(
                collectionsWithCategories = collectionsByType,
                collectionType = collectionType,
                onCategoryTypeChange = collectionsViewModel::changeCategoryType,
                onNavigateToEditCollectionScreen = { collectionOrNull ->
                    collectionViewModel.applyCollection(
                        collection = collectionOrNull ?: collectionsViewModel.getNewCollection()
                    )
                    navViewModel.navigateToScreen(
                        navController = navController,
                        screen = CategoryCollectionsSettingsScreens.EditCategoryCollection
                    )
                },
                onSaveCollectionsButton = {
                    coroutineScope.launch {
                        collectionsViewModel.saveCategoryCollections()
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollection> { backStack ->
            val collectionsViewModel = backStack
                .sharedKoinNavViewModel<EditCategoryCollectionsViewModel>(navController)
            val collectionViewModel = backStack
                .sharedKoinNavViewModel<EditCategoryCollectionViewModel>(navController)

            val collection by collectionViewModel.collectionUiState.collectAsStateWithLifecycle()
            val checkedGroupedCategoriesByType by collectionViewModel.checkedGroupedCategoriesByType
                .collectAsStateWithLifecycle()
            val expandedCategory by collectionViewModel.expandedCategory.collectAsStateWithLifecycle()
            val allowSaving by collectionViewModel.allowSaving.collectAsStateWithLifecycle()

            EditCategoryCollectionScreen(
                collection = collection,
                checkedGroupedCategoriesByType = checkedGroupedCategoriesByType,
                expandedCategory = expandedCategory,
                allowDeleting = collectionViewModel.allowDeleting.value,
                allowSaving = allowSaving,
                onNameChange = collectionViewModel::changeName,
                onCheckedChange = collectionViewModel::inverseCheckedCategoryState,
                onExpandedChange = collectionViewModel::inverseExpandedState,
                onDeleteButton = {
                    collectionsViewModel.deleteCollection(collection = collection)
                    navController.popBackStack()
                },
                onSaveButton = {
                    collectionsViewModel.applyCollection(
                        collection = collectionViewModel.getCollection()
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}