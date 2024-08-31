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
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.SubcategorySetupElement
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.SetupDataScreenContainer

@Composable
fun EditSubcategoriesScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    categoryWithSubcategories: CategoryWithSubcategories?,
    onSaveButton: () -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    SetupDataScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                subcategoryList = categoryWithSubcategories?.subcategoryList ?: emptyList(),
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        },
        smallPrimaryButton = {
            SmallPrimaryButton(text = stringResource(R.string.add_subcategory)) {
                onNavigateToEditCategoryScreen(null)
            }
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(R.string.save),
                onClick = onSaveButton
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    AnimatedContent(
        targetState = subcategoryList,
        label = "subcategory list uploading"
    ) { targetSubcategoryList ->
        if (!WindowTypeIsExpanded) {
            CompactLayout(
                subcategoryList = targetSubcategoryList,
                appTheme = appTheme,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        } else {
            ExpandedLayout(
                subcategoryList = targetSubcategoryList,
                appTheme = appTheme,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        }
    }
}

@Composable
private fun CompactLayout(
    appTheme: AppTheme?,
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
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
        items(items = subcategoryList, key = { it.id }) { category ->
            SubcategorySetupElement(
                category = category,
                appTheme = appTheme,
                onEditButton = {
                    onNavigateToEditCategoryScreen(category)
                },
                onUpButtonClick = {
                    onSwapCategories(category.orderNum, category.orderNum - 1)
                },
                upButtonEnabled = category.orderNum > 1,
                onDownButtonClick = {
                    onSwapCategories(category.orderNum, category.orderNum + 1)
                },
                downButtonEnabled = category.orderNum < subcategoryList.size,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    appTheme: AppTheme?,
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
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
        subcategoryList.forEach { category ->
            Box(modifier = Modifier.padding(9.dp)) {
                SubcategorySetupElement(
                    category = category,
                    appTheme = appTheme,
                    onEditButton = {
                        onNavigateToEditCategoryScreen(category)
                    },
                    onUpButtonClick = {
                        onSwapCategories(category.orderNum, category.orderNum - 1)
                    },
                    upButtonEnabled = category.orderNum > 1,
                    onDownButtonClick = {
                        onSwapCategories(category.orderNum, category.orderNum + 1)
                    },
                    downButtonEnabled = category.orderNum < subcategoryList.size,
                )
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditSubcategoriesScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) { scaffoldPadding ->
        EditSubcategoriesScreen(
            scaffoldPadding = scaffoldPadding,
            appTheme = appTheme,
            categoryWithSubcategories = categoriesWithSubcategories.expense[2],
            onSaveButton = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}
