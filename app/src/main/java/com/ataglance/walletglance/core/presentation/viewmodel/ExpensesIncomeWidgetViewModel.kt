package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.widget.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.utils.getTotalAmountByAccountAndType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ExpensesIncomeWidgetViewModel(
    activeAccount: Account?,
    activeDateRange: TimestampRange,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : ViewModel() {

    private val _activeAccountId = MutableStateFlow(activeAccount?.id)

    fun setActiveAccountId(id: Int) {
        if (_activeAccountId.value == id) return
        _activeAccountId.update { id }
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: TimestampRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return

        _activeDateRange.update { dateRange }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val uiState: StateFlow<ExpensesIncomeWidgetUiState> = combine(
        _transactionsInDateRange,
        _activeAccountId
    ) { transactions, accountId ->
        if (accountId == null) return@combine ExpensesIncomeWidgetUiState()

        ExpensesIncomeWidgetUiState.fromTotal(
            expensesTotal = transactions.getTotalAmountByAccountAndType(
                accountId = accountId, type = CategoryType.Expense
            ),
            incomeTotal = transactions.getTotalAmountByAccountAndType(
                accountId = accountId, type = CategoryType.Income
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ExpensesIncomeWidgetUiState()
    )

}