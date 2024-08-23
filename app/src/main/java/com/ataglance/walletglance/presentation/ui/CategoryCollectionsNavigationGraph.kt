package com.ataglance.walletglance.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.domain.utils.toCollectionsWithIds
import com.ataglance.walletglance.presentation.ui.navigation.screens.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.ui.screens.settings.categoryCollections.EditCategoryCollectionScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.categoryCollections.EditCategoryCollectionsScreen
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel
import com.ataglance.walletglance.presentation.viewmodels.categoryCollections.CategoryCollectionsViewModel
import com.ataglance.walletglance.presentation.viewmodels.categoryCollections.CategoryCollectionsViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.categoryCollections.EditCategoryCollectionViewModel
import com.ataglance.walletglance.presentation.viewmodels.categoryCollections.EditCategoryCollectionViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.sharedViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoryCollectionsGraph(
    navController: NavHostController,
    appViewModel: AppViewModel,
    appTheme: AppTheme?,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsWithIds: CategoryCollectionsWithIds
) {
    navigation<SettingsScreens.CategoryCollections>(
        startDestination = CategoryCollectionsSettingsScreens.EditCategoryCollections
    ) {
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollections> { backStack ->

            val collectionsViewModel = backStack.sharedViewModel<CategoryCollectionsViewModel>(
                navController = navController,
                factory = CategoryCollectionsViewModelFactory(
                    categoryList = categoriesWithSubcategories.concatenateAsCategoryList(),
                    collectionsWithIds = categoryCollectionsWithIds
                )
            )
            val editCollectionViewModel = backStack
                .sharedViewModel<EditCategoryCollectionViewModel>(
                    navController = navController,
                    factory = EditCategoryCollectionViewModelFactory(
                        categoriesWithSubcategories = categoriesWithSubcategories
                    )
                )

            val collectionListByType by collectionsViewModel
                .collectionsWithCategoriesByType.collectAsStateWithLifecycle()
            val categoryCollectionType by collectionsViewModel
                .collectionType.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            EditCategoryCollectionsScreen(
                appTheme = appTheme,
                collectionsWithCategories = collectionListByType,
                collectionType = categoryCollectionType,
                onCategoryTypeChange = collectionsViewModel::changeCategoryType,
                onNavigateToEditCollectionScreen = { collectionOrNull ->
                    editCollectionViewModel.applyCollection(
                        collection = collectionOrNull ?: collectionsViewModel.getNewCollection()
                    )
                    navController.navigate(
                        CategoryCollectionsSettingsScreens.EditCategoryCollection
                    )
                },
                onSaveCollectionsButton = {
                    coroutineScope.launch {
                        appViewModel.saveCategoryCollectionsToDb(
                            collectionsViewModel.getAllCollections().toCollectionsWithIds()
                        )
                        navController.popBackStack()
                    }
                }
//                onSwapCategories = viewModel::swapParentCategories
            )
        }
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollection> { backStack ->

            val collectionsViewModel = backStack.sharedViewModel<CategoryCollectionsViewModel>(
                navController = navController
            )
            val editCollectionViewModel = backStack
                .sharedViewModel<EditCategoryCollectionViewModel>(navController = navController)

            val collectionUiState by editCollectionViewModel
                .collectionUiState.collectAsStateWithLifecycle()
            val editingCategoriesWithSubcategories by editCollectionViewModel
                .editingCategoriesWithSubcategories.collectAsStateWithLifecycle()
            val expandedCategory by editCollectionViewModel
                .expandedCategory.collectAsStateWithLifecycle()
            val allowSaving by editCollectionViewModel
                .allowSaving.collectAsStateWithLifecycle()

            EditCategoryCollectionScreen(
                appTheme = appTheme,
                collection = collectionUiState,
                editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
                expandedCategory = expandedCategory,
                allowDeleting = editCollectionViewModel.allowDeleting.value,
                allowSaving = allowSaving,
                onNameChange = editCollectionViewModel::changeName,
                onCheckedChange = editCollectionViewModel::inverseCheckedCategoryState,
                onExpandedChange = editCollectionViewModel::inverseExpandedState,
                onDeleteButton = {
                    collectionsViewModel.deleteCollection(collectionUiState)
                    navController.popBackStack()
                },
                onSaveButton = {
                    collectionsViewModel.saveEditingCollection(
                        editingCollection = editCollectionViewModel.getCollection()
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}