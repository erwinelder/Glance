package com.ataglance.walletglance.ui.theme.screens.settings.categoryCollections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CheckedCategory
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categories.EditingCategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.EditingCategoryWithSubcategories
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallFilledIconButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.ThreeStateCheckbox
import com.ataglance.walletglance.ui.theme.uielements.categories.RecordCategory
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.dividers.BigDivider
import com.ataglance.walletglance.ui.theme.uielements.dividers.TextDivider
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextFieldWithLabel
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName

@Composable
fun EditCategoryCollectionScreen(
    collection: CategoryCollectionWithCategories,
    editingCategoriesWithSubcategories: EditingCategoriesWithSubcategories,
    expandedCategory: EditingCategoryWithSubcategories?,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit,
    onDeleteButton: () -> Unit,
    onSaveButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
    ) {
        if (allowDeleting) {
            PrimaryButton(text = stringResource(R.string.delete), onClick = onDeleteButton)
        }
        EditCategoryCollectionGlassSurface(
            collection = collection,
            editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
            expandedCategory = expandedCategory,
            onNameChange = onNameChange,
            onCheckedChange = onCheckedChange,
            onExpandedChange = onExpandedChange
        )
        PrimaryButton(
            text = stringResource(R.string.save),
            onClick = onSaveButton,
            enabled = allowSaving
        )
    }
}

@Composable
private fun ColumnScope.EditCategoryCollectionGlassSurface(
    collection: CategoryCollectionWithCategories,
    editingCategoriesWithSubcategories: EditingCategoriesWithSubcategories,
    expandedCategory: EditingCategoryWithSubcategories?,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    val categoriesListState = rememberLazyListState()
    val subcategoriesListState = rememberLazyListState()

    GlassSurface(
        modifier = Modifier.weight(1f),
        filledWidth = if (!WindowTypeIsExpanded) .94f else .86f
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = dimensionResource(R.dimen.field_gap)
                )
        ) {
            CustomTextFieldWithLabel(
                text = collection.name,
                placeholderText = stringResource(R.string.collection_name),
                onValueChange = onNameChange,
                labelText = stringResource(R.string.name)
            )
            AnimatedVisibility(
                visible = expandedCategory == null,
                enter = fadeIn(tween(220, 90)) +
                        scaleIn(tween(220, 90), .95f),
                exit = fadeOut(tween(90))
            ) {
                LazyColumn(
                    state = categoriesListState,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(
                        bottom = dimensionResource(R.dimen.field_gap)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categoryListItems(
                        list = editingCategoriesWithSubcategories.expense,
                        listType = CategoryType.Expense,
                        onCheckedChange = onCheckedChange,
                        onExpandedChange = onExpandedChange
                    )
                    categoryListItems(
                        list = editingCategoriesWithSubcategories.income,
                        listType = CategoryType.Income,
                        onCheckedChange = onCheckedChange,
                        onExpandedChange = onExpandedChange
                    )
                }
            }
            AnimatedVisibility(
                visible = expandedCategory != null,
                enter = fadeIn(tween(220, 90)) +
                        scaleIn(tween(220, 90), .95f),
                exit = fadeOut(tween(90))
            ) {
                if (expandedCategory != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CollectionCategoryItem(
                            category = expandedCategory.category,
                            checked = expandedCategory.checked,
                            expanded = expandedCategory.expanded,
                            onCheckedChange = {
                                onCheckedChange(expandedCategory.category)
                            },
                            onExpandedChange = {
                                onExpandedChange(expandedCategory.category)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BigDivider()
                        LazyColumn(
                            state = subcategoriesListState,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            item {
                                Spacer(modifier = Modifier)
                            }
                            items(
                                items = expandedCategory.subcategoryList,
                                key = { it.category.id }
                            ) { item ->
                                CollectionSubcategoryItem(
                                    checkedCategory = item,
                                    onCheckedChange = {
                                        onCheckedChange(item.category)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.categoryListItems(
    list: List<EditingCategoryWithSubcategories>,
    listType: CategoryType,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    if (list.isNotEmpty()) {
        item {
            TextDivider(
                modifier = Modifier.padding(top = 16.dp),
                textRes = if (listType == CategoryType.Expense) R.string.expenses
                else R.string.income_plural
            )
        }
    }
    items(
        items = list,
        key = { it.category.id }
    ) { item ->
        CollectionCategoryItem(
            category = item.category,
            checked = item.checked,
            expanded = item.expanded.takeIf { item.subcategoryList.isNotEmpty() },
            onCheckedChange = {
                onCheckedChange(item.category)
            },
            onExpandedChange = {
                onExpandedChange(item.category)
            }
        )
    }
}

@Composable
private fun CollectionCategoryItem(
    category: Category,
    checked: Boolean?,
    expanded: Boolean?,
    onCheckedChange: () -> Unit,
    onExpandedChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThreeStateCheckbox(
            state = checked,
            onClick = { onCheckedChange() }
        )
        Spacer(modifier = Modifier.size(10.dp, 48.dp))
        RecordCategory(
            category = category,
            iconSize = 28.dp,
            fontSize = 21.sp
        )
        if (expanded != null) {
            Spacer(modifier = Modifier.width(8.dp))
            AnimatedContent(
                targetState = if (expanded) R.drawable.collapse_icon else R.drawable.expand_icon,
                label = "expand or collapse subcategory list icon",
            ) { iconRes ->
                SmallFilledIconButton(
                    iconRes = iconRes,
                    iconContendDescription = "expand or collapse subcategory list icon",
                    onClick = onExpandedChange
                )
            }
        }
    }
}

@Composable
private fun CollectionSubcategoryItem(
    checkedCategory: CheckedCategory,
    onCheckedChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThreeStateCheckbox(
            state = checkedCategory.checked,
            onClick = { onCheckedChange() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        RecordCategory(
            category = checkedCategory.category,
            iconSize = 28.dp,
            fontSize = 21.sp
        )
    }
}



@Preview
@Composable
private fun EditCategoryCollectionScreenPreview() {
    val categoryList = listOf(
        Category(
            id = 13,
            type = CategoryType.Expense,
            rank = CategoryRank.Sub,
            orderNum = 1,
            parentCategoryId = 1,
            name = "Groceries",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 14,
            type = CategoryType.Expense,
            rank = CategoryRank.Parent,
            orderNum = 2,
            parentCategoryId = null,
            name = "Category 2",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 25,
            type = CategoryType.Expense,
            rank = CategoryRank.Parent,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 30,
            type = CategoryType.Expense,
            rank = CategoryRank.Parent,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        )
    )
    val collection = CategoryCollectionWithCategories(
        id = 1,
        orderNum = 1,
        type = CategoryCollectionType.Expense,
        name = "Collection",
        categoryList = categoryList
    )

    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = AppTheme.LightDefault.name,
            boxWithConstraintsScope = this
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.main_background_light),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                EditCategoryCollectionScreen(
                    collection = collection,
                    editingCategoriesWithSubcategories =
                        DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
                            .toEditingCategoriesWithSubcategories(collection),
                    expandedCategory = null,
                    allowDeleting = true,
                    allowSaving = true,
                    onNameChange = {},
                    onCheckedChange = {},
                    onExpandedChange = {},
                    onDeleteButton = {},
                    onSaveButton = {}
                )
            }
        }
    }
}