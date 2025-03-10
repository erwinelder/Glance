package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsViewModel(
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getBudgetsUseCase.getGroupedByTypeFlow().collect { budgetsByType ->
                _budgetsByType.update { budgetsByType }
            }
        }
    }


    private val _budgetsByType = MutableStateFlow(BudgetsByType())
    val budgetsByType = _budgetsByType.asStateFlow()

}