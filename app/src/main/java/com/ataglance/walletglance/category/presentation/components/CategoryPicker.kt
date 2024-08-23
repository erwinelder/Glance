package com.ataglance.walletglance.category.presentation.components

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
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.WindowTypeIsMedium
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.animation.dialogSlideFromBottomTransition
import com.ataglance.walletglance.core.presentation.animation.dialogSlideToBottomTransition
import com.ataglance.walletglance.core.presentation.components.buttons.CloseButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.dividers.SmallDivider

@Composable
fun CategoryPicker(
    visible: Boolean,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    type: CategoryType,
    appTheme: AppTheme?,
    allowChoosingParentCategory: Boolean = false,
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
                    SmallDivider(color = GlanceTheme.outline.copy(.5f))
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryListItem(categoryWithSubcategories.category, appTheme) {
                        if (
                            categoryWithSubcategories.subcategoryList.isNotEmpty() &&
                                !allowChoosingParentCategory
                        ) {
                            chosenCategoryWithSubcategories = categoryWithSubcategories
                        } else {
                            onCategoryChoose(
                                CategoryWithSubcategory(categoryWithSubcategories.category)
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
                                contentColor = GlanceTheme.primary.copy(.6f),
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = GlanceTheme.outline.copy(.3f)
                            ),
                            onClick = {
                                chosenCategoryWithSubcategories = categoryWithSubcategories
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
                                            GlanceTheme.outline.copy(.3f)
                                        } else {
                                            GlanceTheme.primary.copy(.6f)
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
                        SmallDivider(color = GlanceTheme.outline.copy(.5f))
                    }
                    Row {
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
            }
            SmallDivider(filledWidth = .6f)
            CloseButton(onClick = { chosenCategoryWithSubcategories = null })
        }
    }
}

@Composable
private fun RowScope.CategoryListItem(
    category: Category,
    appTheme: AppTheme?,
    onClick: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f, fill = false)
            .padding(vertical = 12.dp)
    ) {
        RecordCategory(
            category = category,
            appTheme = appTheme,
            iconSize = 30.dp,
            onClick = onClick
        )
    }
}


@Preview
@Composable
private fun CategoryPickerPreview() {
    val appTheme = AppTheme.LightDefault
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()

    PreviewContainer(appTheme = appTheme) {
        CategoryPicker(
            visible = true,
            categoriesWithSubcategories = categoriesWithSubcategories,
            type = CategoryType.Expense,
            appTheme = appTheme,
            allowChoosingParentCategory = true,
            onDismissRequest = {},
            onCategoryChoose = {}
        )
    }
}