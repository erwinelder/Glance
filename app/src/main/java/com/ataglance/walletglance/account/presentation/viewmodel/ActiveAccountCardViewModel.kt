package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActiveAccountCardViewModel(
    private val getTodayTotalExpensesForAccountUseCase: GetTodayTotalExpensesForAccountUseCase
) : ViewModel() {

    private val _todayTotalExpenses = MutableStateFlow(0.0)
    val todayTotalExpenses = _todayTotalExpenses.asStateFlow()

    fun applyActiveAccount(accountId: Int) {
        viewModelScope.launch {
            getTodayTotalExpensesForAccountUseCase.get(accountId = accountId).let {
                _todayTotalExpenses.update { it }
            }
        }
    }

}