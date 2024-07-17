package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.animation.dialogSlideFromBottomTransition
import com.ataglance.walletglance.ui.theme.animation.dialogSlideToBottomTransition
import com.ataglance.walletglance.ui.theme.uielements.buttons.CloseButton
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider

@Composable
fun CategoryPicker(
    visible: Boolean,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    type: CategoryType,
    appTheme: AppTheme?,
    onDismissRequest: () -> Unit,
    onCategoryChoose: (CategoryWithSubcategory) -> Unit
) {
    val parentCategoryListState = rememberLazyListState()
    val subcategoryListState = rememberLazyListState()
    var chosenCategoryWithSubcategories: CategoryWithSubcategories? by remember {
        mutableStateOf(null)
    }
    var subcategoryList: List<Category> by remember { mutableStateOf(emptyList()) }
    chosenCategoryWithSubcategories?.subcategoryList?.takeIf { it.isNotEmpty() }
        ?.let { subcategoryList = it }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)),
        exit = fadeOut(tween(400))
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    onDismissRequest()
                    if (chosenCategoryWithSubcategories != null) {
                        chosenCategoryWithSubcategories = null
                    }
                }
                .fillMaxSize()
                .background(Color.Black.copy(.3f))
        )
    }
    AnimatedVisibility(
        visible = visible && chosenCategoryWithSubcategories == null,
        enter = dialogSlideFromBottomTransition,
        exit = dialogSlideToBottomTransition
    ) {
        LazyColumn(
            state = parentCategoryListState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 84.dp, bottom = dimensionResource(R.dimen.screen_vertical_padding))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
                .background(GlanceTheme.surfaceVariant.copy(1f))
                .fillMaxWidth(
                    when {
                        WindowTypeIsCompact -> .9f
                        WindowTypeIsMedium -> .6f
                        else -> .5f
                    }
                )
        ) {
            items(
                items = categoriesWithSubcategories.getByType(type),
                key = { it.category.id }
            ) { categoryWithSubcategories ->
                if (categoryWithSubcategories.category.orderNum != 1) {
                    SmallDivider()
                }
                CategoryListItem(categoryWithSubcategories.category, appTheme) {
                    if (categoryWithSubcategories.subcategoryList.isNotEmpty()) {
                        chosenCategoryWithSubcategories = categoryWithSubcategories
                    } else {
                        onCategoryChoose(
                            CategoryWithSubcategory(categoryWithSubcategories.category)
                        )
                        onDismissRequest()
                    }
                }
            }
        }
    }
    AnimatedVisibility(
        visible = visible && chosenCategoryWithSubcategories != null,
        enter = dialogSlideFromBottomTransition,
        exit = dialogSlideToBottomTransition
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 84.dp, bottom = dimensionResource(R.dimen.screen_vertical_padding))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
                .background(GlanceTheme.surfaceVariant.copy(1f))
                .fillMaxWidth(
                    when {
                        WindowTypeIsCompact -> .9f
                        WindowTypeIsMedium -> .6f
                        else -> .5f
                    }
                )
                .padding(bottom = 4.dp)
        ) {
            LazyColumn(
                state = subcategoryListState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
            ) {
                items(
                    items = subcategoryList,
                    key = { it.id }
                ) { category ->
                    if (category.orderNum != 1) {
                        SmallDivider()
                    }
                    CategoryListItem(category, appTheme) { subcategory ->
                        chosenCategoryWithSubcategories?.category?.let { parentCategory ->
                            onCategoryChoose(
                                CategoryWithSubcategory(parentCategory, subcategory)
                            )
                            onDismissRequest()
                            if (chosenCategoryWithSubcategories != null) {
                                chosenCategoryWithSubcategories = null
                            }
                        }
                    }
                }
            }
            SmallDivider(filledWidth = .6f)
            CloseButton(onClick = { chosenCategoryWithSubcategories = null })
        }
    }
}

@Composable
private fun CategoryListItem(
    category: Category,
    appTheme: AppTheme?,
    onClick: (Category) -> Unit
) {
    Box(modifier = Modifier.padding(vertical = 12.dp)) {
        RecordCategory(
            category = category,
            appTheme = appTheme,
            iconSize = 30.dp,
            onClick = onClick
        )
    }
}