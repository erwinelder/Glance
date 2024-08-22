package com.ataglance.walletglance.presentation

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
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categories.icons.CategoryPossibleIcons
import com.ataglance.walletglance.presentation.theme.navigation.screens.CategoriesSettingsScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.theme.screens.settings.categories.EditCategoryScreen
import com.ataglance.walletglance.presentation.theme.screens.settings.categories.EditSubcategoryListScreen
import com.ataglance.walletglance.presentation.theme.screens.settings.categories.SetupCategoriesScreen
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel
import com.ataglance.walletglance.presentation.viewmodels.categories.EditCategoriesViewModel
import com.ataglance.walletglance.presentation.viewmodels.categories.EditCategoryViewModel
import com.ataglance.walletglance.presentation.viewmodels.categories.SetupCategoriesViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.sharedViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.categoriesGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
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

            SetupCategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appTheme = appUiSettings.appTheme,
                uiState = categoriesUiState,
                onShowCategoriesByType = categoriesViewModel::changeCategoryTypeToShow,
                onNavigateToEditSubcategoriesScreen = { categoryWithSubcategories ->
                    categoriesViewModel.applySubcategoryListToEdit(categoryWithSubcategories)
                    navController.navigate(CategoriesSettingsScreens.EditSubcategories)
                },
                onNavigateToEditCategoryScreen = { categoryOrNull ->
                    editCategoryViewModel.applyCategory(
                        category = categoryOrNull ?: categoriesViewModel.getNewParentCategory()
                    )
                    navController.navigate(CategoriesSettingsScreens.EditCategory)
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

            EditSubcategoryListScreen(
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
                    navController.navigate(CategoriesSettingsScreens.EditCategory)
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

            val categoryUiState by editCategoryViewModel
                .categoryUiState.collectAsStateWithLifecycle()
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
                },
                categoryIconList = CategoryPossibleIcons().asList()
            )
        }
    }
}