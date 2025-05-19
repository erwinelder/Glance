package com.ataglance.walletglance.category.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsMedium
import com.ataglance.walletglance.core.presentation.animation.dialogSlideFromBottomTransition
import com.ataglance.walletglance.core.presentation.animation.dialogSlideToBottomTransition
import com.ataglance.walletglance.core.presentation.component.button.CloseButton
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect

@Composable
fun CategoryPicker(
    visible: Boolean,
    groupedCategoriesByType: GroupedCategoriesByType,
    type: CategoryType,
    allowChoosingParentCategory: Boolean = false,
    onDismissRequest: () -> Unit,
    onCategoryChoose: (CategoryWithSub) -> Unit
) {
    val parentCategoryListState = rememberLazyListState()
    val subcategoryListState = rememberLazyListState()
    var chosenGroupedCategories: GroupedCategories? by remember {
        mutableStateOf(null)
    }
    var subcategoryList: List<Category> by remember { mutableStateOf(emptyList()) }
    chosenGroupedCategories?.subcategoryList?.takeIf { it.isNotEmpty() }
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
                    if (chosenGroupedCategories != null) {
                        chosenGroupedCategories = null
                    }
                }
                .fillMaxSize()
                .background(Color.Black.copy(.3f))
        )
    }
    AnimatedVisibility(
        visible = visible && chosenGroupedCategories == null,
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
                .background(GlanceColors.surface)
                .fillMaxWidth(
                    when {
                        WindowTypeIsCompact -> .9f
                        WindowTypeIsMedium -> .6f
                        else -> .5f
                    }
                )
        ) {
            items(
                items = groupedCategoriesByType.getByType(type),
                key = { it.category.id }
            ) { categoryWithSubcategories ->
                if (categoryWithSubcategories.category.orderNum != 1) {
                    SmallDivider()
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryListItem(categoryWithSubcategories.category) {
                        if (
                            categoryWithSubcategories.subcategoryList.isNotEmpty() &&
                                !allowChoosingParentCategory
                        ) {
                            chosenGroupedCategories = categoryWithSubcategories
                        } else {
                            onCategoryChoose(
                                CategoryWithSub(categoryWithSubcategories.category)
                            )
                            onDismissRequest()
                        }
                    }
                    if (allowChoosingParentCategory) {
                        FilledIconButton(
                            enabled = categoryWithSubcategories.subcategoryList.isNotEmpty(),
                            shape = RoundedCornerShape(30),
                            colors = IconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = GlanceColors.primary.copy(.6f),
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = GlanceColors.outline.copy(.3f)
                            ),
                            onClick = {
                                chosenGroupedCategories = categoryWithSubcategories
                            },
                            modifier = Modifier.bounceClickEffect()
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.short_arrow_right_icon),
                                contentDescription = "open subcategory list icon",
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (
                                            categoryWithSubcategories.subcategoryList.isEmpty()
                                        ) {
                                            GlanceColors.outline.copy(.3f)
                                        } else {
                                            GlanceColors.primary.copy(.6f)
                                        },
                                        shape = RoundedCornerShape(30)
                                    )
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    AnimatedVisibility(
        visible = visible && chosenGroupedCategories != null,
        enter = dialogSlideFromBottomTransition,
        exit = dialogSlideToBottomTransition
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 84.dp, bottom = dimensionResource(R.dimen.screen_vertical_padding))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
                .background(GlanceColors.surface)
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
                    Row {
                        CategoryListItem(category) { subcategory ->
                            chosenGroupedCategories?.category?.let { parentCategory ->
                                onCategoryChoose(
                                    CategoryWithSub(parentCategory, subcategory)
                                )
                                onDismissRequest()
                                if (chosenGroupedCategories != null) {
                                    chosenGroupedCategories = null
                                }
                            }
                        }
                    }
                }
            }
            SmallDivider(filledWidth = .6f)
            CloseButton(onClick = { chosenGroupedCategories = null })
        }
    }
}

@Composable
private fun RowScope.CategoryListItem(
    category: Category,
    onClick: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f, fill = false)
            .padding(vertical = 12.dp)
    ) {
        RecordCategory(
            category = category,
            iconSize = 30.dp,
            onClick = onClick
        )
    }
}


@Preview
@Composable
private fun CategoryPickerPreview() {
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        CategoryPicker(
            visible = true,
            groupedCategoriesByType = categoriesWithSubcategories,
            type = CategoryType.Expense,
            allowChoosingParentCategory = true,
            onDismissRequest = {},
            onCategoryChoose = {}
        )
    }
}