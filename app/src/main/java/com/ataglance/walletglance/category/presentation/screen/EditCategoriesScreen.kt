package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.navigation.CategoriesSettingsScreens
import com.ataglance.walletglance.category.presentation.component.CategoryTypeBar
import com.ataglance.walletglance.category.presentation.component.EditingParentCategoryComponent
import com.ataglance.walletglance.category.presentation.model.SetupCategoriesUiState
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoryViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.core.utils.takeComposableIf
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun EditCategoriesScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(
        navController = navController,
        parameters = { parametersOf(appConfiguration.langCode) }
    )
    val categoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(navController)

    val uiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        if (uiState.groupedCategories != null) {
            categoriesViewModel.clearSubcategoryList()
        }
    }

    EditCategoriesScreen(
        screenPadding = screenPadding,
        isAppSetUp = appConfiguration.isSetUp,
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
                if (appConfiguration.isSetUp) {
                    navController.popBackStack()
                } else {
                    navViewModel.navigateToScreen(navController, SettingsScreens.Budgets)
                }
            }
        }
    )
}

@Composable
fun EditCategoriesScreen(
    screenPadding: PaddingValues = PaddingValues(),
    isAppSetUp: Boolean,
    uiState: SetupCategoriesUiState,
    onResetButton: () -> Unit,
    onSaveAndFinishSetupButton: () -> Unit,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoriesScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    GlassSurfaceScreenContainer(
        topPadding = screenPadding.calculateTopPadding(),
        bottomPadding = screenPadding.calculateBottomPadding(),
        topBar = {
            CategoryTypeBar(
                currentCategoryType = uiState.categoryType,
                onButtonClick = onShowCategoriesByType
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                uiState = uiState,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoriesScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        },
        glassSurfaceFilledWidths = FilledWidthByScreenType(.86f, .86f, .86f),
        smallPrimaryButton = {
            SmallPrimaryButton(
                text = stringResource(R.string.add_category),
                iconRes = R.drawable.add_icon
            ) {
                onNavigateToEditCategoryScreen(null)
            }
        },
        secondaryBottomButton = takeComposableIf(!isAppSetUp) {
            SecondaryButton(
                text = stringResource(R.string.reset),
                onClick = onResetButton
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(if (isAppSetUp) R.string.save else R.string.save_and_continue),
                onClick = onSaveAndFinishSetupButton
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    uiState: SetupCategoriesUiState,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    AnimatedContent(
        targetState = uiState.categoryType,
        label = "category type"
    ) { categoryType ->
        val categoryWithSubcategoriesList = uiState.groupedCategoriesByType
            .getByType(categoryType)

        if (!WindowTypeIsExpanded) {
            CompactLayout(
                categoryList = categoryWithSubcategoriesList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        } else {
            ExpandedLayout(
                categoryList = categoryWithSubcategoriesList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        }
    }
}

@Composable
private fun CompactLayout(
    categoryList: List<GroupedCategories>,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        itemsIndexed(
            items = categoryList,
            key = { _, item -> item.category.id }
        ) { index, item ->
            EditingParentCategoryComponent(
                category = item.category,
                onNavigateToEditSubcategoryListScreen = {
                    onNavigateToEditSubcategoryListScreen(item)
                },
                onEditButton = { onNavigateToEditCategoryScreen(item.category) },
                onUpButtonClick = { onSwapCategories(index, index - 1) },
                upButtonEnabled = index > 0,
                onDownButtonClick = { onSwapCategories(index, index + 1) },
                downButtonEnabled = index < categoryList.lastIndex,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    categoryList: List<GroupedCategories>,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        categoryList.forEachIndexed { index, item ->
            Box(modifier = Modifier.padding(8.dp)) {
                EditingParentCategoryComponent(
                    category = item.category,
                    onNavigateToEditSubcategoryListScreen = {
                        onNavigateToEditSubcategoryListScreen(item)
                    },
                    onEditButton = { onNavigateToEditCategoryScreen(item.category) },
                    onUpButtonClick = { onSwapCategories(index, index - 1) },
                    upButtonEnabled = index > 0,
                    onDownButtonClick = { onSwapCategories(index, index + 1) },
                    downButtonEnabled = index < categoryList.lastIndex
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoriesScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    categoryType: CategoryType = CategoryType.Expense
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditCategoriesScreen(
            screenPadding = scaffoldPadding,
            isAppSetUp = isAppSetUp,
            uiState = SetupCategoriesUiState(
                categoryType = categoryType,
                groupedCategories = null,
                groupedCategoriesByType = groupedCategoriesByType
            ),
            onResetButton = {},
            onSaveAndFinishSetupButton = {},
            onShowCategoriesByType = {},
            onNavigateToEditSubcategoriesScreen = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}
