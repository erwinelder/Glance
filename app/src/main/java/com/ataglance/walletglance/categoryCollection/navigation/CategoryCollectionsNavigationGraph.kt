package com.ataglance.walletglance.categoryCollection.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreen
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionsScreen
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.CategoryCollectionsViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.CategoryCollectionsViewModelFactory
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModelFactory
import com.ataglance.walletglance.categoryCollection.utils.toCollectionsWithIds
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoryCollectionsGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsWithIdsByType: CategoryCollectionsWithIdsByType
) {
    navigation<SettingsScreens.CategoryCollections>(
        startDestination = CategoryCollectionsSettingsScreens.EditCategoryCollections
    ) {
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollections> { backStack ->
            val collectionsViewModel = backStack.sharedViewModel<CategoryCollectionsViewModel>(
                navController = navController,
                factory = CategoryCollectionsViewModelFactory(
                    categoryList = categoriesWithSubcategories.concatenateAsCategoryList(),
                    collectionsWithIds = categoryCollectionsWithIdsByType
                )
            )
            val editCollectionViewModel = backStack
                .sharedViewModel<EditCategoryCollectionViewModel>(
                    navController = navController,
                    factory = EditCategoryCollectionViewModelFactory(
                        categoriesWithSubcategories = categoriesWithSubcategories
                    )
                )

            val collectionListByType by collectionsViewModel.collectionsWithCategoriesByType
                .collectAsStateWithLifecycle()
            val categoryCollectionType by collectionsViewModel.collectionType
                .collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditCategoryCollectionsScreen(
                collectionWithCategoriesList = collectionListByType,
                collectionType = categoryCollectionType,
                onCategoryTypeChange = collectionsViewModel::changeCategoryType,
                onNavigateToEditCollectionScreen = { collectionOrNull ->
                    editCollectionViewModel.applyCollection(
                        collection = collectionOrNull ?: collectionsViewModel.getNewCollection()
                    )
                    navViewModel.navigateToScreen(
                        navController = navController,
                        screen = CategoryCollectionsSettingsScreens.EditCategoryCollection
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
            val collectionsViewModel = backStack
                .sharedViewModel<CategoryCollectionsViewModel>(navController = navController)
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