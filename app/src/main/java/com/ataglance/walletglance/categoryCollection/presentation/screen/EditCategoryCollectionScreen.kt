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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.mapper.toCheckedCategoriesWithSubcategories
import com.ataglance.walletglance.category.presentation.component.RecordCategory
import com.ataglance.walletglance.category.presentation.model.CheckedCategory
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategories
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategoriesByType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionsViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallFilledIconButton
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.checkbox.ThreeStateCheckbox
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.divider.BigDivider
import com.ataglance.walletglance.core.presentation.component.divider.TextDivider
import com.ataglance.walletglance.core.presentation.component.field.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.utils.takeRowComposableIf
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun EditCategoryCollectionScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController
) {
    val collectionsViewModel = backStack.sharedKoinNavViewModel<EditCategoryCollectionsViewModel>(
        navController = navController
    )
    val collectionViewModel = backStack.sharedKoinNavViewModel<EditCategoryCollectionViewModel>(
        navController = navController
    )

    val collection by collectionViewModel.collectionUiState.collectAsStateWithLifecycle()
    val checkedGroupedCategoriesByType by collectionViewModel.checkedGroupedCategoriesByType
        .collectAsStateWithLifecycle()
    val expandedCategory by collectionViewModel.expandedCategory.collectAsStateWithLifecycle()
    val allowDeleting by collectionViewModel.allowDeleting.collectAsStateWithLifecycle()
    val allowSaving by collectionViewModel.allowSaving.collectAsStateWithLifecycle()

    EditCategoryCollectionScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        collection = collection,
        checkedGroupedCategoriesByType = checkedGroupedCategoriesByType,
        expandedCategory = expandedCategory,
        allowDeleting = allowDeleting,
        allowSaving = allowSaving,
        onNameChange = collectionViewModel::changeName,
        onCheckedChange = collectionViewModel::inverseCheckedCategoryState,
        onExpandedChange = collectionViewModel::inverseExpandedState,
        onDeleteButton = {
            collectionsViewModel.deleteCollection(collection = collection)
            navController.popBackStack()
        },
        onSaveButton = {
            collectionsViewModel.applyCollection(
                collection = collectionViewModel.getCollection()
            )
            navController.popBackStack()
        }
    )
}

@Composable
fun EditCategoryCollectionScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    collection: CategoryCollectionWithCategories,
    checkedGroupedCategoriesByType: CheckedGroupedCategoriesByType,
    expandedCategory: CheckedGroupedCategories?,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit,
    onDeleteButton: () -> Unit,
    onSaveButton: () -> Unit
) {
    val settingsCategory = SettingsCategory.CategoryCollections(appTheme = CurrAppTheme)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        topBackNavButtonText = collection.name,
        topBackNavButtonImageRes = settingsCategory.iconRes,
        onTopBackNavButtonClick = onNavigateBack,
        topBackNavButtonCompanionComponent = takeRowComposableIf(allowDeleting) {
            SmallSecondaryButton(
                text = stringResource(R.string.delete),
                iconRes = R.drawable.trash_icon,
                onClick = onDeleteButton
            )
        },
        primaryButtonText = stringResource(R.string.save),
        primaryButtonEnabled = allowSaving,
        onPrimaryButtonClick = onSaveButton
    ) {
        GlassSurface {
            GlassSurfaceContent(
                collection = collection,
                checkedGroupedCategoriesByType = checkedGroupedCategoriesByType,
                expandedCategory = expandedCategory,
                onNameChange = onNameChange,
                onCheckedChange = onCheckedChange,
                onExpandedChange = onExpandedChange
            )
        }
    }
}

@Composable
private fun GlassSurfaceContent(
    collection: CategoryCollectionWithCategories,
    checkedGroupedCategoriesByType: CheckedGroupedCategoriesByType,
    expandedCategory: CheckedGroupedCategories?,
    onNameChange: (String) -> Unit,
    onCheckedChange: (Category) -> Unit,
    onExpandedChange: (Category) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        paddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp)
    ) {
        TextFieldWithLabel(
            text = collection.name,
            placeholderText = stringResource(R.string.collection_name),
            onValueChange = onNameChange,
            labelText = stringResource(R.string.name)
        )
        ParentCategoriesLists(
            checkedGroupedCategoriesByType = checkedGroupedCategoriesByType,
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
    checkedGroupedCategoriesByType: CheckedGroupedCategoriesByType,
    expandedCategory: CheckedGroupedCategories?,
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
                list = checkedGroupedCategoriesByType.expense,
                listType = CategoryType.Expense,
                onCheckedChange = onCheckedChange,
                onExpandedChange = onExpandedChange
            )
            categoryListItems(
                list = checkedGroupedCategoriesByType.income,
                listType = CategoryType.Income,
                onCheckedChange = onCheckedChange,
                onExpandedChange = onExpandedChange
            )
        }
    }
}

@Composable
private fun SubcategoriesList(
    expandedCategory: CheckedGroupedCategories?,
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
    list: List<CheckedGroupedCategories>,
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



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoryCollectionScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
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
        allCategories = groupedCategoriesByType.asList()
    )
    val editingCategoriesWithSubcategories = groupedCategoriesByType
        .toCheckedCategoriesWithSubcategories(collection)

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        EditCategoryCollectionScreen(
            collection = collection,
            onNavigateBack = {},
            checkedGroupedCategoriesByType = editingCategoriesWithSubcategories,
            expandedCategory = null,
            allowDeleting = false,
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