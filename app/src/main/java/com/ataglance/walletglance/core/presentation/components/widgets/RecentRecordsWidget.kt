package com.ataglance.walletglance.core.presentation.components.widgets

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordsTypeFilter
import com.ataglance.walletglance.record.presentation.components.RecordStackComponent
import com.ataglance.walletglance.record.presentation.components.TransferComponent
import com.ataglance.walletglance.record.utils.containsRecordsFromDifferentYears
import com.ataglance.walletglance.record.utils.getNoRecordsMessageRes

@Composable
fun RecentRecordsWidget(
    recordStackList: List<RecordStack>,
    accountList: List<Account>,
    isCustomDateRange: Boolean,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    recordsTypeFilter: RecordsTypeFilter = RecordsTypeFilter.All,
    onNavigateToRecordsScreen: () -> Unit
) {
    val includeYearToRecordDate =
        isCustomDateRange || recordStackList.containsRecordsFromDifferentYears()

    WidgetWithTitleAndButtonComponent(
        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
        title = stringResource(R.string.recent),
        onBottomNavigationButtonClick = onNavigateToRecordsScreen
    ) {
        AnimatedContent(
            targetState = Pair(recordStackList, recordsTypeFilter),
            label = "records history widget content"
        ) { (targetRecordStackList, targetTypeFilter) ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                RecordStackList(
                    recordStackList = targetRecordStackList,
                    accountList = accountList,
                    includeYearToRecordDate = includeYearToRecordDate,
                    onRecordClick = onRecordClick,
                    onTransferClick = onTransferClick
                )
                if (targetRecordStackList.isEmpty()) {
                    MessageContainer(
                        message = stringResource(targetTypeFilter.getNoRecordsMessageRes())
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
            .height(370.dp)
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
            RecentRecordsWidget(
                recordStackList = listOf(),
                accountList = listOf(),
                isCustomDateRange = false,
                onRecordClick = {},
                onTransferClick = {},
                recordsTypeFilter = RecordsTypeFilter.All,
                onNavigateToRecordsScreen = {}
            )
        }
    }
}
