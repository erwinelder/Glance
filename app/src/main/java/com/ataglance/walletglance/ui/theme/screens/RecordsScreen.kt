package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.ui.theme.screencontainers.DataPresentationScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.categoryCollections.CategoryCollectionTypeToggleButton
import com.ataglance.walletglance.ui.theme.uielements.records.RecordStackComponent
import com.ataglance.walletglance.ui.theme.uielements.records.TransferComponent
import com.ataglance.walletglance.ui.utils.containsRecordsFromDifferentYears
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.toggle
import com.ataglance.walletglance.ui.viewmodels.records.RecordsViewModel

@Composable
fun RecordsScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    viewModel: RecordsViewModel,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val collectionType by viewModel.collectionType.collectAsStateWithLifecycle()
    val filteredRecords by viewModel
        .recordsFilteredByDateAccountAndCollection.collectAsStateWithLifecycle()
    val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
    val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

    val includeYearToRecordDate = filteredRecords.containsRecordsFromDifferentYears()
    val lazyListState = rememberLazyListState()

    DataPresentationScreenContainer(
        scaffoldAppScreenPadding = scaffoldAppScreenPadding,
        accountList = accountList,
        appTheme = appTheme,
        onAccountClick = onAccountClick,
        currentDateRangeEnum = currentDateRangeEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onDateRangeChange = onDateRangeChange,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        collectionList = collectionList,
        selectedCollection = selectedCollection,
        onCollectionSelect = {
            viewModel.selectCollection(it)
        },
        animationContentLabel = "records history widget content",
        animatedContentTargetState = Pair(filteredRecords, collectionType),
        visibleNoDataMessage = filteredRecords.isEmpty(),
        noDataMessageRes = when(collectionType) {
            CategoryCollectionType.Mixed -> R.string.you_have_no_records_in_date_range
            CategoryCollectionType.Expense -> R.string.you_have_no_expenses_in_date_range
            CategoryCollectionType.Income -> R.string.you_have_no_income_in_date_range
        },
        typeToggleButton = {
            CategoryCollectionTypeToggleButton(collectionType) {
                viewModel.setCollectionType(collectionType.toggle())
            }
        },
        onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
        onDimBackgroundChange = onDimBackgroundChange
    ) { targetRecordStackListAndTypeFilter ->
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
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
                        secondAccount = recordStack.stack.firstOrNull()?.note?.toInt()?.let {
                            accountList.findById(it)?.toRecordAccount()
                        },
                        onTransferClick = onTransferClick
                    )
                } else {
                    RecordStackComponent(
                        recordStack = recordStack,
                        includeYearToDate = includeYearToRecordDate,
                        onRecordClick = onRecordClick
                    )
                }
            }
        }
    }
}