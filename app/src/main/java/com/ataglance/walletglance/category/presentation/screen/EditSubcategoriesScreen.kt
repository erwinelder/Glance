package com.ataglance.walletglance.category.presentation.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.EditingSubcategoryComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainer

@Composable
fun EditSubcategoriesScreen(
    scaffoldPadding: PaddingValues,
    categoryWithSubcategories: CategoryWithSubcategories?,
    onSaveButton: () -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    GlassSurfaceScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
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
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    if (!WindowTypeIsExpanded) {
        CompactLayout(
            subcategoryList = subcategoryList,
            onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
            onSwapCategories = onSwapCategories
        )
    } else {
        ExpandedLayout(
            subcategoryList = subcategoryList,
            onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
            onSwapCategories = onSwapCategories
        )
    }
}

@Composable
private fun CompactLayout(
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
        itemsIndexed(items = subcategoryList, key = { _, item -> item.id }) { index, category ->
            EditingSubcategoryComponent(
                category = category,
                onEditButton = {
                    onNavigateToEditCategoryScreen(category)
                },
                onUpButtonClick = {
                    onSwapCategories(index, index - 1)
                },
                upButtonEnabled = index > 0,
                onDownButtonClick = {
                    onSwapCategories(index, index + 1)
                },
                downButtonEnabled = index < subcategoryList.lastIndex,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
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
            .padding(8.dp)
    ) {
        subcategoryList.forEachIndexed { index, category ->
            Box(modifier = Modifier.padding(8.dp)) {
                EditingSubcategoryComponent(
                    category = category,
                    onEditButton = {
                        onNavigateToEditCategoryScreen(category)
                    },
                    onUpButtonClick = {
                        onSwapCategories(index, index - 1)
                    },
                    upButtonEnabled = index > 0,
                    onDownButtonClick = {
                        onSwapCategories(index, index + 1)
                    },
                    downButtonEnabled = index < subcategoryList.lastIndex,
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
            categoryWithSubcategories = categoriesWithSubcategories.expense[2],
            onSaveButton = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}
