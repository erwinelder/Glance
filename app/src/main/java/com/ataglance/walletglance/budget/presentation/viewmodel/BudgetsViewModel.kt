package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.usecase.GetFilledBudgetsByTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsViewModel(
    private val getFilledBudgetsByTypeUseCase: GetFilledBudgetsByTypeUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getFilledBudgetsByTypeUseCase.getAsFlow().collect { budgetsByType ->
                _budgetsByType.update { budgetsByType }
            }
        }
    }


    private val _budgetsByType = MutableStateFlow(BudgetsByType())
    val budgetsByType = _budgetsByType.asStateFlow()

}