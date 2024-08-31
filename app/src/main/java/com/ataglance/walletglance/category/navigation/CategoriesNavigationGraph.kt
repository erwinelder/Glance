package com.ataglance.walletglance.category.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreen
import com.ataglance.walletglance.category.presentation.screen.EditCategoryScreen
import com.ataglance.walletglance.category.presentation.screen.EditSubcategoriesScreen
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoryViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.SetupCategoriesViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoriesGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    categoriesWithSubcategories: CategoriesWithSubcategories
) {
    navigation<SettingsScreens.Categories>(
        startDestination = CategoriesSettingsScreens.EditCategories
    ) {
        composable<CategoriesSettingsScreens.EditCategories> { backStack ->
            val categoriesViewModel = backStack.sharedViewModel<EditCategoriesViewModel>(
                navController = navController,
                factory = SetupCategoriesViewModelFactory(
                    categoriesWithSubcategories = categoriesWithSubcategories
                        .takeIf { it.expense.isNotEmpty() && it.income.isNotEmpty() }
                        ?: DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
                )
            )
            val editCategoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(
                navController = navController
            )

            val categoriesUiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(true) {
                if (categoriesUiState.categoryWithSubcategories != null) {
                    categoriesViewModel.clearSubcategoryList()
                }
            }

            EditCategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                isAppSetUp = appUiSettings.isSetUp,
                uiState = categoriesUiState,
                onShowCategoriesByType = categoriesViewModel::changeCategoryTypeToShow,
                onNavigateToEditSubcategoriesScreen = { categoryWithSubcategories ->
                    categoriesViewModel.applySubcategoryListToEdit(categoryWithSubcategories)
                    navViewModel.navigateToScreen(
                        navController, CategoriesSettingsScreens.EditSubcategories
                    )
                },
                onNavigateToEditCategoryScreen = { categoryOrNull ->
                    editCategoryViewModel.applyCategory(
                        category = categoryOrNull ?: categoriesViewModel.getNewParentCategory()
                    )
                    navViewModel.navigateToScreen(
                        navController, CategoriesSettingsScreens.EditCategory
                    )
                },
                onSwapCategories = categoriesViewModel::swapParentCategories,
                onResetButton = categoriesViewModel::reapplyCategoryLists,
                onSaveAndFinishSetupButton = {
                    coroutineScope.launch {
                        appViewModel.saveCategoriesToDb(
                            categoriesViewModel.getAllCategoryEntities()
                        )
                        if (appUiSettings.isSetUp) navController.popBackStack()
                        else appViewModel.preFinishSetup()
                    }
                }
            )
        }
        composable<CategoriesSettingsScreens.EditSubcategories> { backStack ->
            val categoriesViewModel = backStack.sharedViewModel<EditCategoriesViewModel>(
                navController = navController
            )
            val editCategoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(
                navController = navController
            )

            val categoriesUiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()

            EditSubcategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                categoryWithSubcategories = categoriesUiState.categoryWithSubcategories,
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
                onSwapCategories = categoriesViewModel::swapSubcategories
            )
        }
        composable<CategoriesSettingsScreens.EditCategory> { backStack ->
            val categoriesViewModel = backStack.sharedViewModel<EditCategoriesViewModel>(
                navController = navController
            )
            val editCategoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(
                navController = navController
            )

            val categoryUiState by editCategoryViewModel.categoryUiState
                .collectAsStateWithLifecycle()
            val allowDeleting by editCategoryViewModel.allowDeleting.collectAsStateWithLifecycle()
            val allowSaving by editCategoryViewModel.allowSaving.collectAsStateWithLifecycle()

            EditCategoryScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                category = categoryUiState,
                allowDeleting = allowDeleting,
                allowSaving = allowSaving,
                onNameChange = editCategoryViewModel::changeName,
                onCategoryColorChange = editCategoryViewModel::changeColor,
                onIconChange = editCategoryViewModel::changeIcon,
                onDeleteButton = {
                    categoriesViewModel.deleteCategory(editCategoryViewModel.getCategory())
                    navController.popBackStack()
                },
                onSaveButton = {
                    categoriesViewModel.saveEditedCategory(editCategoryViewModel.getCategory())
                    navController.popBackStack()
                }
            )
        }
    }
}