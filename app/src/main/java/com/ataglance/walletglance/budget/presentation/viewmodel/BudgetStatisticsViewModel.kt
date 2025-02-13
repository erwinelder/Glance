package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalAmountInDateRangesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetStatisticsViewModel(
    budgetId: Int,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val getRecordsTotalAmountInDateRangesUseCase: GetRecordsTotalAmountInDateRangesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.getAll()
            _accounts.update { accounts }

            val budget = getBudgetsUseCase.get(id = budgetId, accounts = accounts)
            _budget.update { budget }

            budget?.category ?: return@launch

            getRecordsTotalAmountInDateRangesUseCase
                .getByCategoryAndAccountsAsFlow(
                    categoryId = budget.category.id,
                    accountsIds = budget.linkedAccountsIds,
                    dateRangeList = budget.repeatingPeriod.getPrevDateRanges()
                )
                .collect { totalInRanges ->
                    _budgetsTotalAmountsInRanges.update {
                        totalInRanges.reversed() + budget.getTotalAmountByCurrentDateRange()
                    }
                }
        }
    }


    private val _budget = MutableStateFlow<Budget?>(null)
    val budget = _budget.asStateFlow()


    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()


    private val _budgetsTotalAmountsInRanges = MutableStateFlow<List<TotalAmountInRange>>(emptyList())
    val budgetTotalAmountsInRanges = _budgetsTotalAmountsInRanges.asStateFlow()

}