package com.ataglance.walletglance.ui.theme.screens.settings.categoryCollections

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.categoryCollections.CategoryCollectionTypeToggleButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface

@Composable
fun SetupCategoryCollectionsScreen(
    collectionsWithCategories: List<CategoryCollectionWithCategories>,
    categoryCollectionType: CategoryCollectionType,
    onCategoryTypeChange: () -> Unit,
    onNavigateToEditCollectionScreen: (Int) -> Unit,
    onAddNewCollection: () -> Unit,
    onSaveCollectionsButton: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(R.dimen.screen_vertical_padding),
                bottom = dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        CategoryCollectionTypeToggleButton(
            currentType = categoryCollectionType,
            onClick = onCategoryTypeChange
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        CategoryCollectionsContainer(
            collectionsWithCategories = collectionsWithCategories,
            onNavigateToEditCategoryCollectionScreen = onNavigateToEditCollectionScreen
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        SmallPrimaryButton(
            onClick = onAddNewCollection,
            text = stringResource(R.string.add_collection)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.widgets_gap)))
        PrimaryButton(
            onClick = onSaveCollectionsButton,
            text = stringResource(R.string.save)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.CategoryCollectionsContainer(
    collectionsWithCategories: List<CategoryCollectionWithCategories>,
    onNavigateToEditCategoryCollectionScreen: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            AnimatedContent(
                targetState = collectionsWithCategories,
                label = "category list uploading"
            ) { targetCollectionsWithCategories ->
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
                           CategoryCollectionSetupComponent(collection) {
                               onNavigateToEditCategoryCollectionScreen(collection.orderNum)
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
                                CategoryCollectionSetupComponent(collection) {
                                    onNavigateToEditCategoryCollectionScreen(collection.orderNum)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCollectionSetupComponent(
    collection: CategoryCollectionWithCategories,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
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
            .padding(horizontal = 15.dp, vertical = 12.dp)
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
}

@Preview
@Composable
private fun SetupCategoryCollectionsScreenPreview() {
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
                SetupCategoryCollectionsScreen(
                    collectionsWithCategories = listOf(
                        CategoryCollectionWithCategories(
                            id = 1,
                            orderNum = 1,
                            type = CategoryCollectionType.Mixed,
                            name = "Category collection collection 1",
                            categoryList = listOf()
                        ),
                        CategoryCollectionWithCategories(
                            id = 2,
                            orderNum = 2,
                            type = CategoryCollectionType.Mixed,
                            name = "Category collection 2",
                            categoryList = listOf()
                        ),
                        CategoryCollectionWithCategories(
                            id = 3,
                            orderNum = 3,
                            type = CategoryCollectionType.Mixed,
                            name = "Category collection 3",
                            categoryList = listOf()
                        ),
                    ),
                    categoryCollectionType = CategoryCollectionType.Mixed,
                    onCategoryTypeChange = {},
                    onNavigateToEditCollectionScreen = {},
                    onAddNewCollection = {},
                    onSaveCollectionsButton = {},
                )
            }
        }
    }
}