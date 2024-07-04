package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryTypeBar
import com.ataglance.walletglance.ui.theme.uielements.categories.ParentCategorySetupElement
import com.ataglance.walletglance.ui.viewmodels.categories.SetupCategoriesUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColumnScope.CategoriesSetupContainer(
    uiState: SetupCategoriesUiState,
    appTheme: AppTheme?,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoryListScreen: (CategoryWithSubcategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit,
    onAddNewCategory: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        CategoryTypeBar(
            currentCategoryType = uiState.categoryType,
            onClick = onShowCategoriesByType
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.button_bar_to_widget_gap)))
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            val categoryWithSubcategoriesList = uiState.getCategoriesWithSubcategoriesListByType()

            AnimatedContent(
                targetState = categoryWithSubcategoriesList,
                label = "category list uploading"
            ) { categoryList ->
                if (!WindowTypeIsExpanded) {
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
                } else {
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
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        SmallPrimaryButton(onClick = onAddNewCategory, text = stringResource(R.string.add_category))
    }
}