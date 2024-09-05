package com.ataglance.walletglance.categoryCollection.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.presentation.components.CategoryCollectionTypeBar
import com.ataglance.walletglance.categoryCollection.presentation.components.EditingCategoryCollectionComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainer

@Composable
fun EditCategoryCollectionsScreen(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    collectionType: CategoryCollectionType,
    onCategoryTypeChange: (CategoryCollectionType) -> Unit,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories?) -> Unit,
    onSaveCollectionsButton: () -> Unit,
) {
    GlassSurfaceContainer(
        topBar = {
            CategoryCollectionTypeBar(
                currentType = collectionType,
                onClick = onCategoryTypeChange
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                collectionWithCategoriesList = collectionWithCategoriesList,
                onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
            )
        },
        smallPrimaryButton = {
            SmallPrimaryButton(
                onClick = {
                    onNavigateToEditCollectionScreen(null)
                },
                text = stringResource(R.string.add_collection)
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                onClick = onSaveCollectionsButton,
                text = stringResource(R.string.save)
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit,
) {
    AnimatedContent(
        targetState = collectionWithCategoriesList,
        label = "category list uploading"
    ) { targetCollectionWithCategoriesList ->
        if (targetCollectionWithCategoriesList.isNotEmpty()) {
            if (!WindowTypeIsExpanded) {
                CompactLayoutContent(
                    collectionWithCategoriesList = targetCollectionWithCategoriesList,
                    onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
                )
            } else {
                ExpandedLayoutContent(
                    collectionWithCategoriesList = targetCollectionWithCategoriesList,
                    onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
                )
            }
        } else {
            MessageContainer(message = stringResource(R.string.no_collections_of_this_type))
        }
    }
}

@Composable
private fun CompactLayoutContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            dimensionResource(R.dimen.widget_content_padding)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        items(
            items = collectionWithCategoriesList,
            key = { it.id }
        ) { collection ->
            EditingCategoryCollectionComponent(collection = collection) {
                onNavigateToEditCollectionScreen(collection)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayoutContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        collectionWithCategoriesList.forEach { collection ->
            Box(modifier = Modifier.padding(9.dp)) {
                EditingCategoryCollectionComponent(collection) {
                    onNavigateToEditCollectionScreen(collection)
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoryCollectionsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    collectionType: CategoryCollectionType = CategoryCollectionType.Mixed,
    categoryCollectionsWithIdsByType: CategoryCollectionsWithIdsByType? = null,
    collectionWithCategoriesList: List<CategoryCollectionWithCategories> =
        categoryCollectionsWithIdsByType?.toCollectionsWithCategories(
            allCategories = categoriesWithSubcategories.concatenateAsCategoryList()
        )?.getByType(collectionType) ?: listOf(
            CategoryCollectionWithCategories(
                id = 1,
                orderNum = 1,
                type = CategoryCollectionType.Mixed,
                name = "Category collection collection 1",
                categoryList = categoriesWithSubcategories.expense.let { list ->
                    list.subList(0, 3).map { it.category }
                }
            ),
            CategoryCollectionWithCategories(
                id = 2,
                orderNum = 2,
                type = CategoryCollectionType.Mixed,
                name = "Collection 2",
                categoryList = categoriesWithSubcategories.expense.let { list ->
                    list.subList(3, 6).map { it.category }
                }
            ),
            CategoryCollectionWithCategories(
                id = 3,
                orderNum = 3,
                type = CategoryCollectionType.Mixed,
                name = "Category collection 3",
                categoryList = categoriesWithSubcategories.expense.let { list ->
                    list.subList(0, list.size).map { it.category }
                }
            ),
        )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) {
        EditCategoryCollectionsScreen(
            collectionWithCategoriesList = collectionWithCategoriesList,
            collectionType = CategoryCollectionType.Mixed,
            onCategoryTypeChange = {},
            onNavigateToEditCollectionScreen = {},
            onSaveCollectionsButton = {},
        )
    }
}