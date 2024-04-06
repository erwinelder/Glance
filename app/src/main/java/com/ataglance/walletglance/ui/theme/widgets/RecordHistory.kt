package com.ataglance.walletglance.ui.theme.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.model.RecordController
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.model.RecordsTypeFilter
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.records.EmptyRecordsHistoryMessageContainer
import com.ataglance.walletglance.ui.theme.uielements.records.RecordStackComponent
import com.ataglance.walletglance.ui.theme.uielements.records.TransferComponent

@Composable
fun RecordHistory(
    recordStackList: List<RecordStack>,
    appTheme: AppTheme?,
    getCategoryAndIcon: (Int, Int?, RecordType?) -> Pair<Category?, Int?>?,
    getAccount: (Int) -> Account?,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    recordsTypeFilter: RecordsTypeFilter = RecordsTypeFilter.All,
    filledWidth: Float? = null,
    title: String = ""
) {
    val includeYearToRecordDate = RecordController().includeYearToRecordDate(recordStackList)
    val lazyListState = rememberLazyListState()

    GlassSurface(filledWidth = filledWidth) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (title.isNotBlank()) 16.dp else 0.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            if (title.isNotBlank()) {
                Text(
                    text = stringResource(R.string.recent),
                    color = GlanceTheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light
                )
            }
            AnimatedContent(
                targetState = Pair(recordStackList, recordsTypeFilter),
                label = "records history widget content"
            ) { targetRecordStackListAndTypeFilter ->
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        top = if (title.isBlank()) 16.dp else 0.dp,
                        bottom = 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(
                        items = targetRecordStackListAndTypeFilter.first,
                        key = { it.recordNum }
                    ) { recordStack ->
                        if (recordStack.isTransfer()) {
                            TransferComponent(
                                recordStack = recordStack,
                                includeYearToDate = includeYearToRecordDate,
                                appTheme = appTheme,
                                getAccount = getAccount,
                                onTransferClick = onTransferClick
                            )
                        } else {
                            RecordStackComponent(
                                recordStack = recordStack,
                                includeYearToDate = includeYearToRecordDate,
                                getCategoryAndIcon = getCategoryAndIcon,
                                getAccount = getAccount,
                                onRecordClick = onRecordClick
                            )
                        }
                    }
                    if (targetRecordStackListAndTypeFilter.first.isEmpty()) {
                        item {
                            EmptyRecordsHistoryMessageContainer(
                                targetRecordStackListAndTypeFilter.second
                            )
                        }
                    }
                }
            }
        }
    }
}