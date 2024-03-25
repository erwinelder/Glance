package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    accountList: List<Account>,
    filteredRecordStackList: List<RecordStack>,
    appTheme: AppTheme?,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    recordsTypeFilter: RecordsTypeFilter,
    onRecordsTypeFilterChange: (RecordsTypeFilter) -> Unit,
    getCategoryAndIcon: (Int, Int?, RecordType?) -> Pair<Category?, Int?>?,
    getAccount: (Int) -> Account?,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.button_bar_to_widget_gap)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = scaffoldAppScreenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding),
                bottom = 20.dp,
                start = 12.dp,
                end = 12.dp
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
            currentTypeFiler = recordsTypeFilter,
            onClick = onRecordsTypeFilterChange
        )
        RecordHistory(
            recordStackList = filteredRecordStackList,
            appTheme = appTheme,
            recordsTypeFilter = recordsTypeFilter,
            getCategoryAndIcon = getCategoryAndIcon,
            getAccount = getAccount,
            onRecordClick = onRecordClick,
            onTransferClick = onTransferClick,
            modifier = Modifier.fillMaxHeight(),
            filledWidth = 1f
        )
    }
}