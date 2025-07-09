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
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryStatisticsGlassComponent
import com.ataglance.walletglance.category.presentation.model.CategoriesStatistics
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
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import kotlinx.datetime.LocalDateTime
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
        viewModel.setActiveAccount(account = appUiState.accountsAndActiveOne.activeAccount)
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
        accounts = accountList,
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
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp + screenPadding.bottom),
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
    accounts: List<Account> = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    ),
    currentDateRangeEnum: DateRangeEnum = DateRangeEnum.ThisMonth,
    isCustomDateRangeWindowOpened: Boolean = false,
    categoryType: CategoryType = CategoryType.Expense,
    currentCollectionType: CategoryCollectionType = CategoryCollectionType.Expense,
    collectionList: List<CategoryCollectionWithIds> = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Expense,
            name = "Essentials",
            categoryIds = listOf(1, 2)
        )
    ),
    selectedCollection: CategoryCollectionWithIds = CategoryCollectionWithIds(
        id = 0,
        orderNum = 0,
        type = CategoryCollectionType.Expense,
        name = "All categories",
        categoryIds = null
    ),
    transactions: List<Transaction> = listOf(
        RecordWithItems(
            record = Record(
                id = 1,
                date = LocalDateTime(2024, 9, 24, 12, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 1,
                    recordId = 1,
                    totalAmount = 68.43,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = "bread, milk"
                ),
                RecordItem(
                    id = 2,
                    recordId = 1,
                    totalAmount = 178.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 24,
                    note = "shampoo"
                )
            )
        ),
        Transfer(
            id = 1,
            date = LocalDateTime(2024, 9, 23, 0, 0).toTimestamp(),
            sender = TransferItem(
                accountId = accounts[0].id,
                amount = 3000.0,
                rate = 1.0
            ),
            receiver = TransferItem(
                accountId = accounts[1].id,
                amount = 3000.0,
                rate = 1.0
            ),
            includeInBudgets = true
        ),
        RecordWithItems(
            record = Record(
                id = 4,
                date = LocalDateTime(2024, 9, 18, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 4,
                    recordId = 4,
                    totalAmount = 120.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 40,
                    note = "Music platform"
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 5,
                date = LocalDateTime(2024, 9, 15, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 5,
                    recordId = 5,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 21,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 6,
                date = LocalDateTime(2024, 9, 12, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 6,
                    recordId = 6,
                    totalAmount = 3599.9,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 7,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 7,
                    recordId = 7,
                    totalAmount = 8500.0,
                    quantity = null,
                    categoryId = 2,
                    subcategoryId = 15,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 8,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Income,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 8,
                    recordId = 8,
                    totalAmount = 42600.0,
                    quantity = null,
                    categoryId = 72,
                    subcategoryId = null,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 9,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 9,
                    recordId = 9,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 38,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 6, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[1].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 450.41,
                    quantity = null,
                    categoryId = 9,
                    subcategoryId = 50,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accounts[0].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 690.56,
                    quantity = null,
                    categoryId = 10,
                    subcategoryId = 58,
                    note = null
                )
            )
        ),
    ),
    parentCategory: CategoryStatistics? = null
) {
    val categoriesStatistics = CategoriesStatistics
        .fromTransactions(
            type = categoryType,
            accountId = accounts[0].id,
            accountCurrency = accounts[0].currency,
            transactions = transactions,
            groupedCategories = groupedCategoriesByType.getByType(type = categoryType)
        )
        .stats

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        CategoryStatisticsScreen(
            screenPadding = scaffoldPadding,
            accountList = accounts,
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
                subcategories = categoriesStatistics
            ),
            onNavigateToEditCollectionsScreen = {},
            onSetParentCategory = {},
            onClearParentCategory = {},

            onDimBackgroundChange = {}
        )
    }
}
