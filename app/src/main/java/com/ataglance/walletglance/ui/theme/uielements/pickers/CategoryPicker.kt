package com.ataglance.walletglance.ui.theme.uielements.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.model.CategoriesUiState
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.uielements.containers.DialogWindow
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CategoryPicker(
    visible: Boolean,
    categoriesUiState: CategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    type: CategoryType,
    onDismissRequest: () -> Unit,
    onCategoryChoose: (Category, Category?) -> Unit,
    parentCategoryListState: LazyListState = rememberLazyListState(),
    subcategoryListState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val showSubcategoryList = remember { mutableStateOf(false) }
    val chosenCategory = remember {
        mutableStateOf(categoriesUiState.parentCategories.expense.first())
    }

    DialogWindow(
        visible = visible,
        onDismissRequest = {
            onDismissRequest()
            if (showSubcategoryList.value) {
                coroutineScope.launch {
                    delay(400)
                    showSubcategoryList.value = false
                }
            }
        }
    ) {
        AnimatedVisibility(
            visible = !showSubcategoryList.value,
            enter = slideInHorizontally(tween(400)) { -(it*1.1).toInt() },
            exit = slideOutHorizontally(tween(400)) { -(it*1.1).toInt() }
        ) {
            CategoryList(
                lazyListState = parentCategoryListState,
                list = if (type == CategoryType.Expense) {
                    categoriesUiState.parentCategories.expense
                } else {
                    categoriesUiState.parentCategories.income
                },
                categoryNameAndIconMap = categoryNameAndIconMap,
                onCategoryClick = { category ->
                    if (category.parentCategoryId != null) {
                        chosenCategory.value = category
                        showSubcategoryList.value = true
                    } else {
                        onCategoryChoose(category, null)
                        onDismissRequest()
                    }
                }
            )
        }
        AnimatedVisibility(
            visible = showSubcategoryList.value,
            enter = slideInHorizontally(tween(400)) { (it*1.1).toInt() },
            exit = slideOutHorizontally(tween(400)) { (it*1.1).toInt() }
        ) {
            CategoryList(
                lazyListState = subcategoryListState,
                list = if (type == CategoryType.Expense) {
                    categoriesUiState.subcategories.expense[chosenCategory.value.orderNum - 1]
                } else {
                    categoriesUiState.subcategories.income[chosenCategory.value.orderNum - 1]
                },
                categoryNameAndIconMap = categoryNameAndIconMap,
                onCategoryClick = { category ->
                    onCategoryChoose(chosenCategory.value, category)
                    onDismissRequest()
                    coroutineScope.launch {
                        delay(400)
                        showSubcategoryList.value = false
                    }
                }
            )
        }
    }
}

@Composable
private fun CategoryList(
    lazyListState: LazyListState,
    list: List<Category>,
    categoryNameAndIconMap: Map<String, Int>,
    onCategoryClick: (Category) -> Unit
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
            .background(GlanceTheme.surfaceVariant.copy(1f))
            .fillMaxWidth(
                when {
                    WindowTypeIsCompact -> 1f
                    WindowTypeIsMedium -> .7f
                    else -> .6f
                }
            )
    ) {
        items(
            items = list,
            key = { it.orderNum }
        ) { category ->
            if (category.orderNum != 1) {
                SmallDivider()
            }
            CategoryListItem(
                category = category,
                iconRes = categoryNameAndIconMap[category.iconName],
            ) {
                onCategoryClick(category)
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    category: Category,
    iconRes: Int?,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        iconRes?.let {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = category.name + " icon",
                tint = GlanceTheme.onSurface,
                modifier = Modifier
                    .size(25.dp)
            )
        }
        Text(
            text = category.name,
            color = GlanceTheme.onSurface,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}