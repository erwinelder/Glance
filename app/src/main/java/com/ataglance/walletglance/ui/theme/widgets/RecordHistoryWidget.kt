package com.ataglance.walletglance.ui.theme.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordsTypeFilter
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.records.EmptyRecordsHistoryMessageContainer
import com.ataglance.walletglance.ui.theme.uielements.records.RecordStackComponent
import com.ataglance.walletglance.ui.theme.uielements.records.TransferComponent
import com.ataglance.walletglance.ui.utils.containsRecordsFromDifferentYears
import com.ataglance.walletglance.ui.utils.findById

@Composable
fun RecordHistoryWidget(
    recordStackList: List<RecordStack>,
    accountList: List<Account>,
    appTheme: AppTheme?,
    isCustomDateRange: Boolean,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    recordsTypeFilter: RecordsTypeFilter = RecordsTypeFilter.All
) {
    val includeYearToRecordDate =
        isCustomDateRange || recordStackList.containsRecordsFromDifferentYears()

    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.recent),
                color = GlanceTheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
            AnimatedContent(
                targetState = Pair(recordStackList, recordsTypeFilter),
                label = "records history widget content"
            ) { targetRecordStackListAndTypeFilter ->
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    RecordStackList(
                        recordStackList = targetRecordStackListAndTypeFilter.first,
                        accountList = accountList,
                        includeYearToRecordDate = includeYearToRecordDate,
                        appTheme = appTheme,
                        onRecordClick = onRecordClick,
                        onTransferClick = onTransferClick
                    )
                    if (targetRecordStackListAndTypeFilter.first.isEmpty()) {
                        EmptyRecordsHistoryMessageContainer()
                    }
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
    appTheme: AppTheme?,
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
                    appTheme = appTheme,
                    recordStack = recordStack,
                    includeYearToDate = includeYearToRecordDate,
                    onRecordClick = onRecordClick
                )
            } else {
                TransferComponent(
                    recordStack = recordStack,
                    includeYearToDate = includeYearToRecordDate,
                    appTheme = appTheme,
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