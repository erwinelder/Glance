package com.ataglance.walletglance.categoryCollection.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.category.utils.toCategoryColorWithName
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WalletGlanceTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.components.screenContainers.SetupDataScreenContainer
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.category.presentation.components.CategoryIconComponent
import com.ataglance.walletglance.categoryCollection.presentation.components.CategoryCollectionTypeBar
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer

@Composable
fun EditCategoryCollectionsScreen(
    appTheme: AppTheme?,
    collectionsWithCategories: List<CategoryCollectionWithCategories>,
    collectionType: CategoryCollectionType,
    onCategoryTypeChange: (CategoryCollectionType) -> Unit,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories?) -> Unit,
    onSaveCollectionsButton: () -> Unit,
) {
    SetupDataScreenContainer(
        topBar = {
            CategoryCollectionTypeBar(
                currentType = collectionType,
                onClick = onCategoryTypeChange
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                collectionsWithCategories = collectionsWithCategories,
                onNavigateToEditCategoryCollectionScreen = onNavigateToEditCollectionScreen
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    collectionsWithCategories: List<CategoryCollectionWithCategories>,
    onNavigateToEditCategoryCollectionScreen: (CategoryCollectionWithCategories) -> Unit,
) {
    AnimatedContent(
        targetState = collectionsWithCategories,
        label = "category list uploading"
    ) { targetCollectionsWithCategories ->
        if (targetCollectionsWithCategories.isNotEmpty()) {
            if (!WindowTypeIsExpanded) {
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
                        items = targetCollectionsWithCategories,
                        key = { it.id }
                    ) { collection ->
                        CategoryCollectionSetupComponent(appTheme, collection) {
                            onNavigateToEditCategoryCollectionScreen(collection)
                        }
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
                    targetCollectionsWithCategories.forEach { collection ->
                        Box(modifier = Modifier.padding(9.dp)) {
                            CategoryCollectionSetupComponent(appTheme, collection) {
                                onNavigateToEditCategoryCollectionScreen(collection)
                            }
                        }
                    }
                }
            }
        } else {
            MessageContainer(message = stringResource(R.string.no_collections_of_this_type))
        }
    }
}

@Composable
private fun CategoryCollectionSetupComponent(
    appTheme: AppTheme?,
    collection: CategoryCollectionWithCategories,
    onClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val categoriesWithUniqueIcons = collection.categoryList
        ?.groupBy { it.icon }
        ?.flatMap { (_, categories) ->
            categories.distinctBy { it.colorWithName }
        }
        ?: emptyList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .bounceClickEffect(.98f, onClick = onClick)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceTheme.onGlassSurfaceGradient,
                    start = Offset(50f, 190f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                1.dp,
                GlanceTheme.outlineVariant,
                RoundedCornerShape(dimensionResource(R.dimen.record_corner_size))
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = collection.name,
                color = GlanceTheme.onSurface,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f, false)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "right arrow",
                tint = GlanceTheme.onSurface,
                modifier = Modifier.size(12.dp, 20.dp)
            )
        }
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(items = categoriesWithUniqueIcons, key = { it.id }) { category ->
                CategoryIconComponent(category, appTheme)
            }
        }
    }
}



@Preview
@Composable
private fun SetupCategoryCollectionsScreenPreview() {
    val categoryList = listOf(
        Category(
            id = 13,
            type = CategoryType.Expense,
            orderNum = 1,
            parentCategoryId = 1,
            name = "Groceries",
            icon = CategoryIcon.Groceries,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 14,
            type = CategoryType.Expense,
            orderNum = 2,
            parentCategoryId = null,
            name = "Category 2",
            icon = CategoryIcon.TravelTickets,
            colorWithName = CategoryColors.Blue.toCategoryColorWithName()
        ),
        Category(
            id = 25,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Sales,
            colorWithName = CategoryColors.Lavender.toCategoryColorWithName()
        ),
        Category(
            id = 30,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Camel.toCategoryColorWithName()
        ),
        Category(
            id = 32,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Red.toCategoryColorWithName()
        ),
        Category(
            id = 31,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Clothes,
            colorWithName = CategoryColors.Pink.toCategoryColorWithName()
        ),
        Category(
            id = 33,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Red.toCategoryColorWithName()
        )
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
                EditCategoryCollectionsScreen(
                    appTheme = AppTheme.LightDefault,
                    collectionsWithCategories = listOf(
                        CategoryCollectionWithCategories(
                            id = 1,
                            orderNum = 1,
                            type = CategoryCollectionType.Mixed,
                            name = "Category collection collection 1",
                            categoryList = categoryList
                        ),
                        CategoryCollectionWithCategories(
                            id = 2,
                            orderNum = 2,
                            type = CategoryCollectionType.Mixed,
                            name = "Collection 2",
                            categoryList = categoryList
                        ),
                        CategoryCollectionWithCategories(
                            id = 3,
                            orderNum = 3,
                            type = CategoryCollectionType.Mixed,
                            name = "Category collection 3",
                            categoryList = categoryList
                        ),
                    ),
                    collectionType = CategoryCollectionType.Mixed,
                    onCategoryTypeChange = {},
                    onNavigateToEditCollectionScreen = {},
                    onSaveCollectionsButton = {},
                )
            }
        }
    }
}