package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.components.CategoryStatisticsItemComponent
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsByType
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.model.GroupedCategoryStatistics
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.component.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.component.screenContainers.GlassSurfaceScreenContainerWithFilters
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.utils.getCurrentDateLong
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import com.ataglance.walletglance.record.mapper.toRecordStacks

@Composable
fun CategoryStatisticsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    collectionsUiState: CategoryCollectionsUiState,
    onToggleCollectionType: () -> Unit,
    onCollectionSelect: (Int) -> Unit,

    groupedCategoryStatistics: GroupedCategoryStatistics,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onSetParentCategory: (CategoryStatistics) -> Unit,
    onClearParentCategory: () -> Unit,

    onDimBackgroundChange: (Boolean) -> Unit
) {
    GlassSurfaceScreenContainerWithFilters(
        screenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionsUiState = collectionsUiState,
        onCollectionSelect = onCollectionSelect,
        onToggleCollectionType = onToggleCollectionType,
        animatedContentLabel = "all categories statistics",
        animatedContentTargetState = groupedCategoryStatistics,
        visibleNoDataMessage = groupedCategoryStatistics.subcategories.isEmpty(),
        noDataMessageRes = R.string.no_data_for_the_selected_filter,
        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
        onDimBackgroundChange = onDimBackgroundChange
    ) { groupedStatistics ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            groupedStatistics.parentCategory?.let {
                Spacer(modifier = Modifier.height(16.dp))
                CategoryStatisticsItemComponent(
                    uiState = it,
                    showLeftArrow = true,
                    onClick = onClearParentCategory
                )
                Spacer(modifier = Modifier.height(16.dp))
                BigDivider()
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = groupedStatistics.subcategories,
                    key = { it.category.id }
                ) { category ->
                    CategoryStatisticsItemComponent(uiState = category) {
                        onSetParentCategory(category)
                    }
                }
            }
        }
    }
}



@Preview(
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun CategoryStatisticsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isBottomBarVisible: Boolean = true,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    currentDateRangeEnum: DateRangeEnum = DateRangeEnum.ThisMonth,
    isCustomDateRangeWindowOpened: Boolean = false,
    currentCollectionType: CategoryCollectionType = CategoryCollectionType.Expense,
    collectionList: List<CategoryCollectionWithIds> = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Expense,
            name = "Essentials",
            categoriesIds = listOf(1, 2)
        )
    ),
    selectedCollection: CategoryCollectionWithIds = CategoryCollectionWithIds(
        id = 0,
        orderNum = 0,
        type = CategoryCollectionType.Expense,
        name = "All categories",
        categoriesIds = null
    ),
    recordEntityList: List<RecordEntity>? = null,
    recordStackList: List<RecordStack> = recordEntityList?.toRecordStacks(
        accounts = accountList,
        groupedCategoriesByType = groupedCategoriesByType
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getCurrentDateLong(),
            type = RecordType.Expense,
            account = accountList[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = DefaultCategoriesPackage(LocalContext.current)
                        .getDefaultCategories().expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        )
    ),
    parentCategory: CategoryStatistics? = null
) {
    val categoriesStatisticsByType = CategoriesStatisticsByType
        .fromRecordStacks(recordStackList.filterByCollection(selectedCollection))
        .getByType(currentCollectionType)

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.CategoryStatistics()) }
    ) { scaffoldPadding ->
        CategoryStatisticsScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            accountList = accountList,
            onAccountClick = {},
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = {},
            onCustomDateRangeButtonClick = {},

            collectionsUiState = CategoryCollectionsUiState(
                collections = collectionList,
                activeType = currentCollectionType,
                activeCollection = selectedCollection
            ),
            onToggleCollectionType = {},
            onCollectionSelect = {},

            groupedCategoryStatistics = GroupedCategoryStatistics(
                parentCategory = parentCategory,
                subcategories = categoriesStatisticsByType
            ),
            onNavigateToEditCollectionsScreen = {},
            onSetParentCategory = {},
            onClearParentCategory = {},

            onDimBackgroundChange = {}
        )
    }
}
