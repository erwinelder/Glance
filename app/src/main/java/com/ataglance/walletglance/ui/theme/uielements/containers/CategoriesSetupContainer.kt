package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.model.SetupCategoriesUiState
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.categories.ParentCategorySetupElement

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColumnScope.CategoriesSetupContainer(
    uiState: SetupCategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoryListScreen: (Int) -> Unit,
    onNavigateToEditCategoryScreen: (Int) -> Unit,
    onSwapCategories: (Int, Int) -> Unit,
    onAddNewCategory: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(if (WindowTypeIsCompact) .82f else .56f)
        ) {
            BarButton(
                onClick = { onShowCategoriesByType(CategoryType.Income) },
                active = uiState.categoryTypeToShow == CategoryType.Income,
                text = stringResource(R.string.income_plural)
            )
            BarButton(
                onClick = { onShowCategoriesByType(CategoryType.Expense) },
                active = uiState.categoryTypeToShow == CategoryType.Expense,
                text = stringResource(R.string.expenses)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.button_bar_to_widget_gap)))
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            val categoryList = if (uiState.categoryTypeToShow == CategoryType.Expense) {
                uiState.expenseParentCategoryList
            } else {
                uiState.incomeParentCategoryList
            }

            AnimatedContent(
                targetState = categoryList,
                label = "category list uploading"
            ) { targetCategoryList ->
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
                        items(items = targetCategoryList, key = { it.orderNum }) { category ->
                            ParentCategorySetupElement(
                                category = category,
                                iconRes = categoryNameAndIconMap[category.iconName],
                                onNavigateToEditSubcategoryListScreen = {
                                    onNavigateToEditSubcategoryListScreen(category.orderNum)
                                },
                                onEditButton = {
                                    onNavigateToEditCategoryScreen(category.orderNum)
                                },
                                onUpButtonClick = { onSwapCategories(category.orderNum, category.orderNum - 1) },
                                upButtonEnabled = category.orderNum > 1,
                                onDownButtonClick = { onSwapCategories(category.orderNum, category.orderNum + 1) },
                                downButtonEnabled = category.orderNum < targetCategoryList.size,
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
                        targetCategoryList.forEach { category ->
                            Box(modifier = Modifier.padding(9.dp)) {
                                ParentCategorySetupElement(
                                    category = category,
                                    iconRes = categoryNameAndIconMap[category.iconName],
                                    onNavigateToEditSubcategoryListScreen = {
                                        onNavigateToEditSubcategoryListScreen(category.orderNum)
                                    },
                                    onEditButton = {
                                        onNavigateToEditCategoryScreen(category.orderNum)
                                    },
                                    onUpButtonClick = { onSwapCategories(category.orderNum, category.orderNum - 1) },
                                    upButtonEnabled = category.orderNum > 1,
                                    onDownButtonClick = { onSwapCategories(category.orderNum, category.orderNum + 1) },
                                    downButtonEnabled = category.orderNum < targetCategoryList.size,
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