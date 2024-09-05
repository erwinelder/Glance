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
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CheckedCategory
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.EditingCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.EditingCategoryWithSubcategories
import com.ataglance.walletglance.category.presentation.components.RecordCategory
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallFilledIconButton
import com.ataglance.walletglance.core.presentation.components.buttons.ThreeStateCheckbox
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.components.dividers.TextDivider
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainer

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
    GlassSurfaceContainer(
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
    collection: CategoryCollectionWithCategories,
    editingCategoriesWithSubcategories: EditingCategoriesWithSubcategories,
    expandedCategory: EditingCategoryWithSubcategories?,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
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
        TextFieldWithLabel(
            text = collection.name,
            placeholderText = stringResource(R.string.collection_name),
            onValueChange = onNameChange,
            labelText = stringResource(R.string.name)
        )
        ParentCategoriesLists(
            editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
            expandedCategory = expandedCategory,
            onCheckedChange = onCheckedChange,
            onExpandedChange = onExpandedChange
        )
        SubcategoriesList(
            expandedCategory = expandedCategory,
            onCheckedChange = onCheckedChange,
            onExpandedChange = onExpandedChange
        )
    }
}

@Composable
private fun ParentCategoriesLists(
    editingCategoriesWithSubcategories: EditingCategoriesWithSubcategories,
    expandedCategory: EditingCategoryWithSubcategories?,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    val categoriesListState = rememberLazyListState()

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
}

@Composable
private fun SubcategoriesList(
    expandedCategory: EditingCategoryWithSubcategories?,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    val subcategoriesListState = rememberLazyListState()

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

private fun LazyListScope.categoryListItems(
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
fun EditCategoryCollectionScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    collectionWithIds: CategoryCollectionWithIds = CategoryCollectionWithIds(
        id = 1,
        orderNum = 1,
        type = CategoryCollectionType.Expense,
        name = "Collection",
        categoriesIds = listOf(13, 14, 25, 30)
    ),
) {
    val collection = collectionWithIds.toCategoryCollectionWithCategories(
        allCategories = categoriesWithSubcategories.concatenateAsCategoryList()
    )
    val editingCategoriesWithSubcategories = categoriesWithSubcategories
        .toEditingCategoriesWithSubcategories(collection)

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) {
        EditCategoryCollectionScreen(
            collection = collection,
            editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
            expandedCategory = null,
            allowDeleting = true,
            allowSaving = collection.allowSaving() &&
                    editingCategoriesWithSubcategories.hasCheckedCategory(),
            onNameChange = {},
            onCheckedChange = {},
            onExpandedChange = {},
            onDeleteButton = {},
            onSaveButton = {}
        )
    }
}