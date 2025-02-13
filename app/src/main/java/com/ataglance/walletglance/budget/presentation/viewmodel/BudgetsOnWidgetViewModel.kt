package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsOnWidgetViewModel(
    private val getBudgetsOnWidgetUseCase: GetBudgetsOnWidgetUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getBudgetsOnWidgetUseCase.getAsFlow().collect { budgets ->
                _budgets.update { budgets }
            }
        }
    }


    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets = _budgets.asStateFlow()

}