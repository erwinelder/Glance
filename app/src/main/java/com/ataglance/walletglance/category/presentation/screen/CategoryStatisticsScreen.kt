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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryStatisticsGlassComponent
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsByType
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.model.GroupedCategoryStatistics
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.divider.BigDivider
import com.ataglance.walletglance.core.presentation.component.screenContainer.GlassSurfaceScreenContainerWithFilters
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import com.ataglance.walletglance.record.mapper.toRecordStacks
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CategoryStatisticsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    appUiState: AppUiState,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val parentCategoryId = backStack.toRoute<MainScreens.CategoryStatistics>().parentCategoryId
    val categoryType = backStack.toRoute<MainScreens.CategoryStatistics>().type
    val defaultCollectionName = stringResource(R.string.all_categories)

    val viewModel = koinViewModel<CategoryStatisticsViewModel> {
        parametersOf(
            parentCategoryId,
            CategoryType.valueOf(categoryType),
            appUiState.accountsAndActiveOne.activeAccount,
            appUiState.dateRangeWithEnum.dateRange,
            defaultCollectionName
        )
    }

    LaunchedEffect(appUiState.accountsAndActiveOne.activeAccount) {
        viewModel.setActiveAccountId(id = appUiState.accountsAndActiveOne.activeAccount?.id ?: 0)
    }
    LaunchedEffect(appUiState.dateRangeWithEnum.dateRange) {
        viewModel.setActiveDateRange(dateRange = appUiState.dateRangeWithEnum.dateRange)
    }

    val collectionsUiState by viewModel.categoryCollectionsUiState.collectAsStateWithLifecycle()
    val groupedCategoryStatistics by viewModel.groupedCategoryStatistics.collectAsStateWithLifecycle()

    CategoryStatisticsScreen(
        screenPadding = screenPadding,
        accountList = appUiState.accountsAndActiveOne.accounts,
        onAccountClick = appViewModel::applyActiveAccount,
        currentDateRangeEnum = appUiState.dateRangeWithEnum.enum,
        isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
        onDateRangeChange = appViewModel::selectDateRange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionsUiState = collectionsUiState,
        onToggleCollectionType = viewModel::toggleCollectionType,
        onCollectionSelect = viewModel::selectCollection,

        groupedCategoryStatistics = groupedCategoryStatistics,
        onNavigateToEditCollectionsScreen = {
            navViewModel.navigateToScreenMovingTowardsLeft(
                navController = navController,
                screen = CategoryCollectionsSettingsScreens.EditCategoryCollections
            )
        },
        onSetParentCategory = viewModel::setParentCategoryStatistics,
        onClearParentCategory = viewModel::clearParentCategoryStatistics,

        onDimBackgroundChange = onDimBackgroundChange
    )
}

@Composable
fun CategoryStatisticsScreen(
    screenPadding: PaddingValues = PaddingValues(),
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
        screenPadding = screenPadding,
        accountList = accountList,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionsUiState = collectionsUiState,
        onCollectionSelect = onCollectionSelect,
        onToggleCollectionType = onToggleCollectionType,
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
                Spacer(modifier = Modifier.height(8.dp))
                CategoryStatisticsGlassComponent(
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
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = groupedStatistics.subcategories,
                    key = { it.category.id }
                ) { category ->
                    CategoryStatisticsGlassComponent(uiState = category) {
                        onSetParentCategory(category)
                    }
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun CategoryStatisticsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
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
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 2,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[1].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 3,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[2].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 4,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[3].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 5,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[4].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 6,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[5].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 7,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[6].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
    ),
    parentCategory: CategoryStatistics? = null
) {
    val categoriesStatisticsByType = CategoriesStatisticsByType
        .fromRecordStacks(recordStacks = recordStackList.filterByCollection(selectedCollection))
        .getByType(type = currentCollectionType)

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        CategoryStatisticsScreen(
            screenPadding = scaffoldPadding,
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
