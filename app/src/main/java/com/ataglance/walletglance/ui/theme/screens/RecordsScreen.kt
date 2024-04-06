package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.model.RecordsTypeFilter
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.DateFilterBar
import com.ataglance.walletglance.ui.theme.uielements.containers.RecordsTypeFilterBar
import com.ataglance.walletglance.ui.theme.uielements.containers.SmallAccountsContainer
import com.ataglance.walletglance.ui.theme.widgets.RecordHistory

@Composable
fun RecordsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountList: List<Account>,
    recordStackList: List<RecordStack>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    getCategoryAndIcon: (Int, Int?, RecordType?) -> Pair<Category?, Int?>?,
    getAccount: (Int) -> Account?,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
) {
    var recordsType by remember { mutableStateOf(RecordsTypeFilter.All) }
    val filteredRecordStackList = when (recordsType) {
        RecordsTypeFilter.All -> recordStackList
        RecordsTypeFilter.Expenses -> recordStackList.filter { it.isExpenseOrOutTransfer() }
        RecordsTypeFilter.Income -> recordStackList.filter { it.isIncomeOrInTransfer() }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.button_bar_to_widget_gap)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = scaffoldAppScreenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.button_bar_to_widget_gap),
                bottom = scaffoldAppScreenPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding),
                start = 16.dp,
                end = 16.dp
            )
    ) {
        SmallAccountsContainer(
            accountList = accountList,
            appTheme = appTheme,
            onAccountClick = onAccountClick
        )
        DateFilterBar(
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = onDateRangeChange,
            onCustomDateRangeButtonClick = onCustomDateRangeButtonClick
        )
        RecordsTypeFilterBar(
            currentTypeFiler = recordsType,
            onClick = {
                recordsType = it
            }
        )
        Spacer(modifier = Modifier)
        RecordHistory(
            recordStackList = filteredRecordStackList,
            appTheme = appTheme,
            recordsTypeFilter = recordsType,
            getCategoryAndIcon = getCategoryAndIcon,
            getAccount = getAccount,
            onRecordClick = onRecordClick,
            onTransferClick = onTransferClick,
            modifier = Modifier.fillMaxHeight(),
            filledWidth = 1f
        )
    }
}