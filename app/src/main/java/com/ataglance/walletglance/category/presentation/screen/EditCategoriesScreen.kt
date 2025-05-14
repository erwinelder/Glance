package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.CategoryTypeBar
import com.ataglance.walletglance.category.presentation.components.EditingParentCategoryComponent
import com.ataglance.walletglance.category.presentation.model.SetupCategoriesUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainers.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.utils.takeComposableIf

@Composable
fun EditCategoriesScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    uiState: SetupCategoriesUiState,
    onResetButton: () -> Unit,
    onSaveAndFinishSetupButton: () -> Unit,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoriesScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    GlassSurfaceScreenContainer(
        topPadding = scaffoldPadding.takeUnless { isAppSetUp }?.calculateTopPadding(),
        topBar = {
            CategoryTypeBar(
                currentCategoryType = uiState.categoryType,
                onButtonClick = onShowCategoriesByType
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                uiState = uiState,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoriesScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        },
        glassSurfaceFilledWidths = FilledWidthByScreenType(.86f, .86f, .86f),
        smallPrimaryButton = {
            SmallPrimaryButton(text = stringResource(R.string.add_category)) {
                onNavigateToEditCategoryScreen(null)
            }
        },
        secondaryBottomButton = takeComposableIf(!isAppSetUp) {
            SecondaryButton(text = stringResource(R.string.reset), onClick = onResetButton)
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(if (isAppSetUp) R.string.save else R.string.save_and_continue),
                onClick = onSaveAndFinishSetupButton
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    uiState: SetupCategoriesUiState,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    AnimatedContent(
        targetState = uiState.categoryType,
        label = "category type"
    ) { categoryType ->
        val categoryWithSubcategoriesList = uiState.groupedCategoriesByType
            .getByType(categoryType)

        if (!WindowTypeIsExpanded) {
            CompactLayout(
                categoryList = categoryWithSubcategoriesList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        } else {
            ExpandedLayout(
                categoryList = categoryWithSubcategoriesList,
                onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        }
    }
}

@Composable
private fun CompactLayout(
    categoryList: List<GroupedCategories>,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        itemsIndexed(
            items = categoryList,
            key = { _, item -> item.category.id }
        ) { index, item ->
            EditingParentCategoryComponent(
                category = item.category,
                onNavigateToEditSubcategoryListScreen = {
                    onNavigateToEditSubcategoryListScreen(item)
                },
                onEditButton = { onNavigateToEditCategoryScreen(item.category) },
                onUpButtonClick = { onSwapCategories(index, index - 1) },
                upButtonEnabled = index > 0,
                onDownButtonClick = { onSwapCategories(index, index + 1) },
                downButtonEnabled = index < categoryList.lastIndex,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    categoryList: List<GroupedCategories>,
    onNavigateToEditSubcategoryListScreen: (GroupedCategories) -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        categoryList.forEachIndexed { index, item ->
            Box(modifier = Modifier.padding(8.dp)) {
                EditingParentCategoryComponent(
                    category = item.category,
                    onNavigateToEditSubcategoryListScreen = {
                        onNavigateToEditSubcategoryListScreen(item)
                    },
                    onEditButton = { onNavigateToEditCategoryScreen(item.category) },
                    onUpButtonClick = { onSwapCategories(index, index - 1) },
                    upButtonEnabled = index > 0,
                    onDownButtonClick = { onSwapCategories(index, index + 1) },
                    downButtonEnabled = index < categoryList.lastIndex
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoriesScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    categoryType: CategoryType = CategoryType.Expense
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditCategoriesScreen(
            scaffoldPadding = scaffoldPadding,
            isAppSetUp = isAppSetUp,
            uiState = SetupCategoriesUiState(
                categoryType = categoryType,
                groupedCategories = null,
                groupedCategoriesByType = groupedCategoriesByType
            ),
            onResetButton = {},
            onSaveAndFinishSetupButton = {},
            onShowCategoriesByType = {},
            onNavigateToEditSubcategoriesScreen = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}
