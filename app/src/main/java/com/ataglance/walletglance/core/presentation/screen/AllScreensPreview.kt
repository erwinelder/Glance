package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.utils.toAccountColorWithName
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenPreview
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.presentation.screen.RecordsScreenPreview
import com.ataglance.walletglance.record.utils.asChar

private val appTheme: AppTheme = AppTheme.LightDefault
private const val isAppSetup: Boolean = true
private const val isSetupProgressTopBarVisible: Boolean = false
private const val isBottomBarVisible: Boolean = true
private val accountsUiState: AccountsUiState = AccountsUiState(
    accountList = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD Card",
            currency = "USD",
            balance = 1516.41,
            color = AccountPossibleColors().pink.toAccountColorWithName(),
            isActive = true
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Czech Local Card",
            currency = "CZK",
            balance = 43551.63,
            color = AccountPossibleColors().blue.toAccountColorWithName(),
            isActive = false
        )
    ),
    activeAccount = Account(
        id = 1,
        orderNum = 1,
        name = "Main USD Card",
        currency = "USD",
        balance = 1516.41,
        color = AccountPossibleColors().pink.toAccountColorWithName(),
        isActive = true
    )
)
private val dateRangeMenuUiState = DateRangeEnum.ThisMonth.getDateRangeMenuUiState()
private const val isCustomDateRangeWindowOpened = false
private val recordEntityList = listOf(
    RecordEntity(
        id = 1,
        recordNum = 1,
        date = getTodayDateLong(),
        type = RecordType.Expense.asChar(),
        accountId = 1,
        amount = 42.43,
        quantity = null,
        categoryId = 1,
        subcategoryId = 13,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 2,
        recordNum = 2,
        date = getTodayDateLong(),
        type = RecordType.Expense.asChar(),
        accountId = 1,
        amount = 42.43,
        quantity = null,
        categoryId = 1,
        subcategoryId = 14,
        note = null,
        includeInBudgets = true
    ),
)
private val categoryCollectionsWithIdsByType = CategoryCollectionsWithIdsByType(
    expense = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Expense,
            name = "Essentials",
            categoriesIds = listOf(1, 2)
        )
    ),
    income = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Income,
            name = "Essentials",
            categoriesIds = listOf(1, 2)
        )
    ),
    mixed = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Mixed,
            name = "Essentials",
            categoriesIds = listOf(1, 2)
        )
    ),
)
private val budgetEntityList = listOf(
    BudgetEntity(
        id = 1,
        amountLimit = 1000.0,
        categoryId = 1,
        name = "Food & drinks",
        repeatingPeriod = RepeatingPeriod.Monthly.name,
    ),
    BudgetEntity(
        id = 2,
        amountLimit = 250.0,
        categoryId = 1,
        name = "Food & drinks",
        repeatingPeriod = RepeatingPeriod.Weekly.name,
    ),
    BudgetEntity(
        id = 3,
        amountLimit = 2000.0,
        categoryId = 3,
        name = "Shopping",
        repeatingPeriod = RepeatingPeriod.Yearly.name,
    ),
)
private val budgetAccountAssociationList = listOf(
    BudgetAccountAssociation(budgetId = 1, accountId = 1),
    BudgetAccountAssociation(budgetId = 1, accountId = 2),
    BudgetAccountAssociation(budgetId = 2, accountId = 1),
    BudgetAccountAssociation(budgetId = 2, accountId = 2),
    BudgetAccountAssociation(budgetId = 3, accountId = 1),
)

@Preview(
    name = "HomeScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentHomeScreenPreview() {
    HomeScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountsUiState = accountsUiState,
        dateRangeMenuUiState = dateRangeMenuUiState,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        recordList = recordEntityList
    )
}

@Preview(
    name = "RecordsScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentRecordsScreenPreview() {
    val context = LocalContext.current
    val collectionType = CategoryCollectionType.Mixed
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByType(collectionType)

    RecordsScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsUiState.accountList,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        collectionType = collectionType,
        collectionList = categoryCollectionWithIdsList,
        recordList = recordEntityList
    )
}

@Preview(
    name = "CategoryStatisticsScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentCategoryStatisticsScreenPreview() {
    val context = LocalContext.current
    val currentCategoryType = CategoryType.Expense
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByCategoryType(currentCategoryType)

    CategoryStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsUiState.accountList,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        currentCategoryType = currentCategoryType,
        parentCategory = null,
        collectionList = categoryCollectionWithIdsList,
        selectedCollection = categoryCollectionWithIdsList[0],
        recordEntityList = recordEntityList
    )
}

@Preview(
    name = "BudgetsScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentBudgetsScreenPreview() {
    BudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        budgetEntityList = budgetEntityList,
        budgetAccountAssociationList = budgetAccountAssociationList,
        accountList = accountsUiState.accountList,
        recordList = recordEntityList
    )
}

@Preview(
    name = "BudgetStatisticsScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentBudgetStatisticsScreenPreview() {
    BudgetStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsUiState.accountList
    )
}

@Preview(
    name = "FinishSetupScreen",
    group = "MainScreens",
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentFinishSetupScreenPreview() {
    SetupFinishScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    )
}
