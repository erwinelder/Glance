package com.ataglance.walletglance.category.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreen
import com.ataglance.walletglance.category.presentation.screen.EditCategoryScreen
import com.ataglance.walletglance.category.presentation.screen.EditSubcategoriesScreen
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoryViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoriesGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    isAppSetUp: Boolean
) {
    navigation<SettingsScreens.Categories>(
        startDestination = CategoriesSettingsScreens.EditCategories
    ) {
        composable<CategoriesSettingsScreens.EditCategories> { backStack ->
            val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(navController)
            val categoryViewModel = backStack.sharedKoinNavViewModel<EditCategoryViewModel>(navController)

            val uiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(true) {
                if (uiState.groupedCategories != null) {
                    categoriesViewModel.clearSubcategoryList()
                }
            }

            EditCategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = isAppSetUp,
                uiState = uiState,
                onShowCategoriesByType = categoriesViewModel::changeCategoryType,
                onNavigateToEditSubcategoriesScreen = { categoryWithSubcategories ->
                    categoriesViewModel.applySubcategoryListToEdit(categoryWithSubcategories)
                    navViewModel.navigateToScreen(
                        navController, CategoriesSettingsScreens.EditSubcategories
                    )
                },
                onNavigateToEditCategoryScreen = { categoryOrNull ->
                    categoryViewModel.applyCategory(
                        category = categoryOrNull ?: categoriesViewModel.getNewParentCategory()
                    )
                    navViewModel.navigateToScreen(
                        navController, CategoriesSettingsScreens.EditCategory
                    )
                },
                onSwapCategories = categoriesViewModel::moveParentCategories,
                onResetButton = categoriesViewModel::reapplyCategoryLists,
                onSaveAndFinishSetupButton = {
                    coroutineScope.launch {
                        categoriesViewModel.saveCategories()
                        if (isAppSetUp) {
                            navController.popBackStack()
                        } else {
                            navViewModel.navigateToScreen(navController, SettingsScreens.Budgets)
                        }
                    }
                }
            )
        }
        composable<CategoriesSettingsScreens.EditSubcategories> { backStack ->
            val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(navController)
            val editCategoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(navController)

            val categoriesUiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()

            EditSubcategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                groupedCategories = categoriesUiState.groupedCategories,
                onSaveButton = {
                    categoriesViewModel.saveSubcategoryList()
                    navController.popBackStack()
                },
                onNavigateToEditCategoryScreen = { categoryOrNull ->
                    editCategoryViewModel.applyCategory(
                        category = categoryOrNull ?: categoriesViewModel.getNewSubcategory()
                    )
                    navViewModel.navigateToScreen(
                        navController, CategoriesSettingsScreens.EditCategory
                    )
                },
                onSwapCategories = categoriesViewModel::moveSubcategories
            )
        }
        composable<CategoriesSettingsScreens.EditCategory> { backStack ->
            val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(navController)
            val categoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(navController)

            val categoryUiState by categoryViewModel.categoryUiState.collectAsStateWithLifecycle()
            val allowDeleting by categoryViewModel.allowDeleting.collectAsStateWithLifecycle()
            val allowSaving by categoryViewModel.allowSaving.collectAsStateWithLifecycle()

            EditCategoryScreen(
                scaffoldPadding = scaffoldPadding,
                category = categoryUiState,
                allowDeleting = allowDeleting,
                allowSaving = allowSaving,
                onNameChange = categoryViewModel::changeName,
                onCategoryColorChange = categoryViewModel::changeColor,
                onIconChange = categoryViewModel::changeIcon,
                onDeleteButton = {
                    categoriesViewModel.deleteCategory(categoryViewModel.getCategory())
                    navController.popBackStack()
                },
                onSaveButton = {
                    categoriesViewModel.saveEditedCategory(categoryViewModel.getCategory())
                    navController.popBackStack()
                }
            )
        }
    }
}