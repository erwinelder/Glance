package com.ataglance.walletglance.transaction.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetViewAllButton
import com.ataglance.walletglance.core.presentation.component.widget.container.WidgetContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.transaction.presentation.model.RecentTransactionsWidgetUiState
import com.ataglance.walletglance.transaction.presentation.viewmodel.RecentTransactionsWidgetViewModel
import com.ataglance.walletglance.record.presentation.component.RecordOnGlassComponent
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import com.ataglance.walletglance.transaction.mapper.toUiStates
import com.ataglance.walletglance.transaction.presentation.model.RecordUiState
import com.ataglance.walletglance.transaction.presentation.model.TransactionTypeFilter
import com.ataglance.walletglance.transaction.presentation.model.TransactionUiState
import com.ataglance.walletglance.transaction.presentation.model.TransferUiState
import com.ataglance.walletglance.transaction.presentation.utils.getNoRecordsMessageRes
import com.ataglance.walletglance.transfer.presentation.component.TransferOnGlassComponent
import kotlinx.datetime.LocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecentTransactionsWidgetWrapper(
    accountsAndActiveOne: AccountsAndActiveOne,
    dateRangeWithEnum: DateRangeWithEnum,
    onRecordClick: (Long) -> Unit,
    onTransferClick: (Long) -> Unit,
    onNavigateToRecordsScreen: () -> Unit
) {
    val viewModel = koinViewModel<RecentTransactionsWidgetViewModel> {
        parametersOf(accountsAndActiveOne.activeAccount, dateRangeWithEnum.dateRange)
    }

    LaunchedEffect(accountsAndActiveOne.activeAccount) {
        accountsAndActiveOne.activeAccount?.id?.let(viewModel::setActiveAccountId)
    }
    LaunchedEffect(dateRangeWithEnum.dateRange) {
        viewModel.setActiveDateRange(dateRange = dateRangeWithEnum.dateRange)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecentTransactionsWidget(
        uiState = uiState,
        onRecordClick = onRecordClick,
        onTransferClick = onTransferClick,
        onNavigateToRecordsScreen = onNavigateToRecordsScreen
    )
}

@Composable
fun RecentTransactionsWidget(
    uiState: RecentTransactionsWidgetUiState,
    onRecordClick: (Long) -> Unit,
    onTransferClick: (Long) -> Unit,
    onNavigateToRecordsScreen: () -> Unit
) {
    WidgetContainer(
        title = stringResource(R.string.recent),
        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
        buttonsBlock = {
            WidgetViewAllButton(onClick = onNavigateToRecordsScreen)
        }
    ) {
        AnimatedContent(targetState = uiState) { state ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.height(370.dp)
            ) {
                RecordStackList(
                    transactions = state.asList(),
                    onRecordClick = onRecordClick,
                    onTransferClick = onTransferClick
                )
                if (state.isEmpty()) {
                    MessageContainer(
                        message = stringResource(TransactionTypeFilter.All.getNoRecordsMessageRes())
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordStackList(
    transactions: List<TransactionUiState>,
    onRecordClick: (Long) -> Unit,
    onTransferClick: (Long) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        items(items = transactions) { transaction ->
            when (transaction) {
                is RecordUiState -> RecordOnGlassComponent(
                    uiState = transaction, onRecordClick = onRecordClick
                )
                is TransferUiState -> TransferOnGlassComponent(
                    uiState = transaction, onClick = onTransferClick
                )
            }
        }
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun RecentRecordsWidgetPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val resourceManager = ResourceManagerImpl(context = LocalContext.current)

    val accounts = listOf(
        Account(id = 1, orderNum = 1, isActive = true),
        Account(id = 2, orderNum = 2, isActive = false)
    )
    val defaultCategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()

    val transactions = listOf(
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
    )

    val uiState = RecentTransactionsWidgetUiState.fromTransactions(
        transactions = transactions.toUiStates(
            accountId = accounts[0].id,
            accounts = accounts,
            groupedCategoriesByType = defaultCategories,
            resourceManager = resourceManager
        )
    )

    PreviewContainer(appTheme = appTheme) {
        Column {
            RecentTransactionsWidget(
                uiState = uiState,
                onRecordClick = {},
                onTransferClick = {},
                onNavigateToRecordsScreen = {}
            )
        }
    }
}
