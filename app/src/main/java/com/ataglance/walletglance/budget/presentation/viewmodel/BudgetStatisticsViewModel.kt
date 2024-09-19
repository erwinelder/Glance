package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetStatisticsViewModel(
    passedBudget: Budget?,
    budgetUsedAmountByRangeList: Flow<List<TotalAmountByRange>>
) : ViewModel() {

    private val _budgetsTotalAmountsByRanges: MutableStateFlow<List<TotalAmountByRange>> =
        MutableStateFlow(emptyList())
    val budgetsTotalAmountsByRanges: StateFlow<List<TotalAmountByRange>> =
        _budgetsTotalAmountsByRanges.asStateFlow()

    init {
        viewModelScope.launch {
            budgetUsedAmountByRangeList.collect { amountsByRanges ->
                _budgetsTotalAmountsByRanges.update {
                    amountsByRanges.reversed() + (passedBudget?.let { budget ->
                        listOf(budget.getTotalAmountByCurrentDateRange())
                    } ?: emptyList())
                }
            }
        }
    }

}

data class BudgetStatisticsViewModelFactory(
    private val budget: Budget?,
    private val usedAmountByRangeList: Flow<List<TotalAmountByRange>>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BudgetStatisticsViewModel(budget, usedAmountByRangeList) as T
    }
}