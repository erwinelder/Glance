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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.CategoryTypeBar
import com.ataglance.walletglance.category.presentation.components.ParentCategorySetupElement
import com.ataglance.walletglance.category.presentation.viewmodel.SetupCategoriesUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.SetupDataScreenContainer

@Composable
fun EditCategoriesScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    isAppSetUp: Boolean,
    uiState: SetupCategoriesUiState,
    onResetButton: () -> Unit,
    onSaveAndFinishSetupButton: () -> Unit,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoriesScreen: (CategoryWithSubcategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    SetupDataScreenContainer(
        topPadding = scaffoldPadding.takeUnless { isAppSetUp }?.calculateTopPadding(),
        topBar = {
            CategoryTypeBar(
                currentCategoryType = uiState.categoryType,
                onClick = onShowCategoriesByType
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                uiState = uiState,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoriesScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        },
        smallPrimaryButton = {
            SmallPrimaryButton(text = stringResource(R.string.add_category)) {
                onNavigateToEditCategoryScreen(null)
            }
        },
        secondaryBottomButton = if (!isAppSetUp) {
            {
                SecondaryButton(text = stringResource(R.string.reset), onClick = onResetButton)
            }
        } else null,
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(if (isAppSetUp) R.string.save else R.string.save_and_finish),
                onClick = onSaveAndFinishSetupButton
            )
        }
    )
}

@Composable
fun GlassSurfaceContent(
    appTheme: AppTheme?,
    uiState: SetupCategoriesUiState,
    onNavigateToEditSubcategoryListScreen: (CategoryWithSubcategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val categoryWithSubcategoriesList = uiState.getCategoriesWithSubcategoriesListByType()

    AnimatedContent(
        targetState = categoryWithSubcategoriesList,
        label = "category list uploading"
    ) { categoryList ->
        if (!WindowTypeIsExpanded) {
            CompactLayout(
                appTheme = appTheme,
                categoryList = categoryList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        } else {
            ExpandedLayout(
                appTheme = appTheme,
                categoryList = categoryList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        }
    }
}

@Composable
private fun CompactLayout(
    appTheme: AppTheme?,
    categoryList: List<CategoryWithSubcategories>,
    onNavigateToEditSubcategoryListScreen: (CategoryWithSubcategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(dimensionResource(R.dimen.widget_content_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        items(
            items = categoryList,
            key = { it.category.id }
        ) { item ->
            ParentCategorySetupElement(
                category = item.category,
                appTheme = appTheme,
                onNavigateToEditSubcategoryListScreen = {
                    onNavigateToEditSubcategoryListScreen(item)
                },
                onEditButton = {
                    onNavigateToEditCategoryScreen(item.category)
                },
                onUpButtonClick = {
                    onSwapCategories(
                        item.category.orderNum, item.category.orderNum - 1
                    )
                },
                upButtonEnabled = item.category.orderNum > 1,
                onDownButtonClick = {
                    onSwapCategories(
                        item.category.orderNum, item.category.orderNum + 1
                    )
                },
                downButtonEnabled = item.category.orderNum < categoryList.size,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    appTheme: AppTheme?,
    categoryList: List<CategoryWithSubcategories>,
    onNavigateToEditSubcategoryListScreen: (CategoryWithSubcategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        categoryList.forEach { item ->
            Box(modifier = Modifier.padding(9.dp)) {
                ParentCategorySetupElement(
                    category = item.category,
                    appTheme = appTheme,
                    onNavigateToEditSubcategoryListScreen = {
                        onNavigateToEditSubcategoryListScreen(item)
                    },
                    onEditButton = {
                        onNavigateToEditCategoryScreen(item.category)
                    },
                    onUpButtonClick = {
                        onSwapCategories(
                            item.category.orderNum, item.category.orderNum - 1
                        )
                    },
                    upButtonEnabled = item.category.orderNum > 1,
                    onDownButtonClick = {
                        onSwapCategories(
                            item.category.orderNum, item.category.orderNum + 1
                        )
                    },
                    downButtonEnabled = item.category.orderNum < categoryList.size,
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
    isSetupProgressTopBarVisible: Boolean = false,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    categoryType: CategoryType = CategoryType.Expense
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) { scaffoldPadding ->
        EditCategoriesScreen(
            scaffoldPadding = scaffoldPadding,
            appTheme = appTheme,
            isAppSetUp = isAppSetUp,
            uiState = SetupCategoriesUiState(
                categoryType = categoryType,
                categoryWithSubcategories = null,
                categoriesWithSubcategories = categoriesWithSubcategories
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
