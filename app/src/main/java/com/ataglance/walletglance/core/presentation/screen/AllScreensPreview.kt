package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.utils.toAccountColorWithName
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackUnit
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.presentation.screen.RecordsScreenPreview

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
private val recordStackList = listOf(
    RecordStack(
        recordNum = 1,
        date = getTodayDateLong(),
        type = RecordType.Expense,
        account = Account().toRecordAccount(),
        totalAmount = 42.43,
        stack = listOf(
            RecordStackUnit(
                id = 1,
                amount = 46.47,
                quantity = null,
                categoryWithSubcategory = null,
                note = null,
                includeInBudgets = true
            )
        )
    )
)
private val collectionList = listOf(
    CategoryCollectionWithIds(
        id = 1,
        orderNum = 1,
        type = CategoryCollectionType.Mixed,
        name = "Essentials",
        categoriesIds = listOf(1, 3)
    )
)

@Preview(
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
    )
}

@Preview(
    apiLevel = 34,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentRecordsScreenPreview() {
    RecordsScreenPreview(
        appTheme = appTheme,
        isAppSetup = isAppSetup,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsUiState.accountList,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
    )
}