package com.ataglance.walletglance.categoryCollection.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CheckedCategory
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.EditingCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.EditingCategoryWithSubcategories
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.category.utils.toCategoryColorWithName
import com.ataglance.walletglance.core.presentation.components.screenContainers.SetupDataScreenContainer
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallFilledIconButton
import com.ataglance.walletglance.core.presentation.components.buttons.ThreeStateCheckbox
import com.ataglance.walletglance.category.presentation.components.RecordCategory
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.components.dividers.TextDivider
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel

@Composable
fun EditCategoryCollectionScreen(
    appTheme: AppTheme?,
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
    SetupDataScreenContainer(
        topButton = if (allowDeleting) {
            {
                SecondaryButton(
                    text = stringResource(R.string.delete),
                    onClick = onDeleteButton
                )
            }
        } else null,
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                collection = collection,
                editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
                expandedCategory = expandedCategory,
                onNameChange = onNameChange,
                onCheckedChange = onCheckedChange,
                onExpandedChange = onExpandedChange
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(R.string.save),
                onClick = onSaveButton,
                enabled = allowSaving
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    collection: CategoryCollectionWithCategories,
    editingCategoriesWithSubcategories: EditingCategoriesWithSubcategories,
    expandedCategory: EditingCategoryWithSubcategories?,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    val categoriesListState = rememberLazyListState()
    val subcategoriesListState = rememberLazyListState()

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
            TextFieldWithLabel(
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
                        appTheme = appTheme,
                        list = editingCategoriesWithSubcategories.expense,
                        listType = CategoryType.Expense,
                        onCheckedChange = onCheckedChange,
                        onExpandedChange = onExpandedChange
                    )
                    categoryListItems(
                        appTheme = appTheme,
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
                            appTheme = appTheme,
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
                                    appTheme = appTheme,
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

private fun LazyListScope.categoryListItems(
    appTheme: AppTheme?,
    list: List<EditingCategoryWithSubcategories>,
    listType: CategoryType,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    if (list.isNotEmpty()) {
        item {
            TextDivider(
                textRes = if (listType == CategoryType.Expense) R.string.expenses
                    else R.string.income_plural,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
    items(
        items = list,
        key = { it.category.id }
    ) { item ->
        CollectionCategoryItem(
            appTheme = appTheme,
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
    appTheme: AppTheme?,
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
            appTheme = appTheme,
            iconSize = 32.dp,
            fontSize = 20.sp
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
    appTheme: AppTheme?,
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
            appTheme = appTheme,
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
            orderNum = 1,
            parentCategoryId = 1,
            name = "Groceries",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 14,
            type = CategoryType.Expense,
            orderNum = 2,
            parentCategoryId = null,
            name = "Category 2",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 25,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 30,
            type = CategoryType.Expense,
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

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        EditCategoryCollectionScreen(
            appTheme = AppTheme.LightDefault,
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