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
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.categories.SubcategorySetupElement

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColumnScope.EditSubcategoryListContainer(
    subcategoryList: List<Category>,
    appTheme: AppTheme?,
    onNavigateToEditCategoryScreen: (Int) -> Unit,
    onSwapCategories: (Int, Int) -> Unit,
    onAddNewSubcategory: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            AnimatedContent(
                targetState = subcategoryList,
                label = "subcategory list uploading"
            ) { targetSubcategoryList ->
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
                        items(items = targetSubcategoryList, key = { it.id }) { category ->
                            SubcategorySetupElement(
                                category = category,
                                appTheme = appTheme,
                                onEditButton = {
                                    onNavigateToEditCategoryScreen(category.orderNum)
                                },
                                onUpButtonClick = { onSwapCategories(category.orderNum, category.orderNum - 1) },
                                upButtonEnabled = category.orderNum > 1,
                                onDownButtonClick = { onSwapCategories(category.orderNum, category.orderNum + 1) },
                                downButtonEnabled = category.orderNum < targetSubcategoryList.size,
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
                        targetSubcategoryList.forEach { category ->
                            Box(modifier = Modifier.padding(9.dp)) {
                                SubcategorySetupElement(
                                    category = category,
                                    appTheme = appTheme,
                                    onEditButton = {
                                        onNavigateToEditCategoryScreen(category.orderNum)
                                    },
                                    onUpButtonClick = { onSwapCategories(category.orderNum, category.orderNum - 1) },
                                    upButtonEnabled = category.orderNum > 1,
                                    onDownButtonClick = { onSwapCategories(category.orderNum, category.orderNum + 1) },
                                    downButtonEnabled = category.orderNum < targetSubcategoryList.size,
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        SmallPrimaryButton(onClick = onAddNewSubcategory, text = stringResource(R.string.add_subcategory))
    }
}