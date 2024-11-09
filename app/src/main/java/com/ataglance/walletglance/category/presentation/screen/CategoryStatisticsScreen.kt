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
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryStatisticsElementUiState
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.CategoryStatisticsItemComponent
import com.ataglance.walletglance.category.presentation.components.CategoryTypeToggleButton
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainerWithFilterBars
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.mapper.toRecordStackList
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.domain.utils.filterByCollection

@Composable
fun CategoryStatisticsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    parentCategory: CategoryStatisticsElementUiState?,
    categoryStatisticsList: List<CategoryStatisticsElementUiState>,
    currentCategoryType: CategoryType,
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (CategoryCollectionWithIds) -> Unit,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onSetCategoryType: (CategoryType) -> Unit,
    onSetParentCategory: (CategoryStatisticsElementUiState) -> Unit,
    onClearParentCategory: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    GlassSurfaceContainerWithFilterBars(
        screenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionList = collectionList,
        selectedCollection = selectedCollection,
        onCollectionSelect = onCollectionSelect,
        typeToggleButton = {
            CategoryTypeToggleButton(currentType = currentCategoryType, onClick = onSetCategoryType)
        },
        animatedContentLabel = "all categories statistics",
        animatedContentTargetState = Pair(categoryStatisticsList, parentCategory),
        visibleNoDataMessage = categoryStatisticsList.isEmpty(),
        noDataMessageRes = R.string.no_data_for_the_selected_filter,
        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
        onDimBackgroundChange = onDimBackgroundChange
    ) { (categoryList, parentCategory) ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            parentCategory?.let {
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
                items(items = categoryList, key = { it.category.id }) { category ->
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
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    currentDateRangeEnum: DateRangeEnum = DateRangeEnum.ThisMonth,
    isCustomDateRangeWindowOpened: Boolean = false,
    currentCategoryType: CategoryType = CategoryType.Expense,
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
    recordStackList: List<RecordStack> = recordEntityList?.toRecordStackList(
        accountList = accountList,
        categoriesWithSubcategories = categoriesWithSubcategories
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getTodayDateLong(),
            type = RecordType.Expense,
            account = accountList[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSubcategory = DefaultCategoriesPackage(LocalContext.current)
                        .getDefaultCategories().expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        )
    ),
    parentCategory: CategoryStatisticsElementUiState? = null
) {
    val categoryStatisticsLists = categoriesWithSubcategories.getStatistics(
        recordStackList = recordStackList.filterByCollection(selectedCollection)
    ).getByType(currentCategoryType)

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.CategoryStatistics(0)) }
    ) { scaffoldPadding ->
        CategoryStatisticsScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            accountList = accountList,
            onAccountClick = {},
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = {},
            onCustomDateRangeButtonClick = {},
            currentCategoryType = currentCategoryType,
            parentCategory = parentCategory,
            categoryStatisticsList = categoryStatisticsLists,
            collectionList = collectionList,
            selectedCollection = selectedCollection,
            onNavigateToEditCollectionsScreen = {},
            onCollectionSelect = {},
            onSetCategoryType = {},
            onSetParentCategory = {},
            onClearParentCategory = {},
            onDimBackgroundChange = {}
        )
    }
}
