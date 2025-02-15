package com.ataglance.walletglance.record.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.widgets.WidgetWithTitleAndButtonComponent
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordsTypeFilter
import com.ataglance.walletglance.record.presentation.model.RecentRecordsWidgetUiState
import com.ataglance.walletglance.record.presentation.utils.getNoRecordsMessageRes
import com.ataglance.walletglance.record.presentation.viewmodel.RecentRecordsWidgetViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecentRecordsWidget(
    accountsAndActiveOne: AccountsAndActiveOne,
    dateRangeWithEnum: DateRangeWithEnum,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    onNavigateToRecordsScreen: () -> Unit
) {
    val viewModel = koinViewModel<RecentRecordsWidgetViewModel> {
        parametersOf(accountsAndActiveOne.activeAccount, dateRangeWithEnum.dateRange)
    }

    LaunchedEffect(accountsAndActiveOne.activeAccount) {
        accountsAndActiveOne.activeAccount?.id?.let(viewModel::setActiveAccountId)
    }
    LaunchedEffect(dateRangeWithEnum.dateRange) {
        viewModel.setActiveDateRange(dateRangeWithEnum.dateRange)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecentRecordsWidgetContent(
        uiState = uiState,
        accountList = accountsAndActiveOne.accounts,
        isCustomDateRange = dateRangeWithEnum.enum == DateRangeEnum.Custom,
        onRecordClick = onRecordClick,
        onTransferClick = onTransferClick,
        onNavigateToRecordsScreen = onNavigateToRecordsScreen
    )
}

@Composable
fun RecentRecordsWidgetContent(
    uiState: RecentRecordsWidgetUiState,
    accountList: List<Account>,
    isCustomDateRange: Boolean,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    onNavigateToRecordsScreen: () -> Unit
) {
    val includeYearToRecordDate = isCustomDateRange || uiState.containsRecordsFromDifferentYears()

    WidgetWithTitleAndButtonComponent(
        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
        title = stringResource(R.string.recent),
        onBottomNavigationButtonClick = onNavigateToRecordsScreen
    ) {
        AnimatedContent(targetState = uiState) { state ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(370.dp)
            ) {
                RecordStackList(
                    recordStackList = state.asList(),
                    accountList = accountList,
                    includeYearToRecordDate = includeYearToRecordDate,
                    onRecordClick = onRecordClick,
                    onTransferClick = onTransferClick
                )
                if (state.isEmpty()) {
                    MessageContainer(
                        message = stringResource(RecordsTypeFilter.All.getNoRecordsMessageRes())
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordStackList(
    recordStackList: List<RecordStack>,
    accountList: List<Account>,
    includeYearToRecordDate: Boolean,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
//            .height(370.dp)
            .verticalScroll(ScrollState(0), enabled = false)
    ) {
        for (recordStack in recordStackList) {
            if (recordStack.isExpenseOrIncome()) {
                RecordStackComponent(
                    recordStack = recordStack,
                    includeYearToDate = includeYearToRecordDate,
                    onRecordClick = onRecordClick
                )
            } else {
                TransferComponent(
                    recordStack = recordStack,
                    includeYearToDate = includeYearToRecordDate,
                    secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                        accountList.findById(it)?.toRecordAccount()
                    },
                    onTransferClick = onTransferClick
                )
            }
        }
        if (recordStackList.isNotEmpty()) {
            Spacer(Modifier)
        }
    }
}



@Preview
@Composable
private fun RecordHistoryWidgetPreview() {
    PreviewContainer {
        Column {
            RecentRecordsWidgetContent(
                uiState = RecentRecordsWidgetUiState(),
                accountList = listOf(),
                isCustomDateRange = false,
                onRecordClick = {},
                onTransferClick = {},
                onNavigateToRecordsScreen = {}
            )
        }
    }
}
