package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsUseCase
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.budget.copyDataToBudget
import com.ataglance.walletglance.budget.mapper.budget.toNewBudget
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBudgetsViewModel(
    private val saveBudgetsUseCase: SaveBudgetsUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val budgetsByType = getBudgetsUseCase.getGroupedByType()
            _budgetsByType.update { budgetsByType }
        }
    }


    private val _budgetsByType = MutableStateFlow(BudgetsByType())
    val budgetsByType: StateFlow<BudgetsByType> = _budgetsByType.asStateFlow()

    fun applyBudget(budgetUiState: BudgetDraft) {
        val budgetsByType = budgetsByType.value

        val newBudgetsByType = if (budgetUiState.isNew) {
            budgetUiState.toNewBudget()
                ?.let { budgetsByType.addBudget(it) }
                ?: budgetsByType
        } else {
            budgetsByType.concatenate().replaceById(budgetUiState).groupByType()
        }

        _budgetsByType.update { newBudgetsByType }
    }

    fun deleteBudget(id: Int, repeatingPeriod: RepeatingPeriod) {
        val newBudgetsByType = budgetsByType.value.deleteBudget(id, repeatingPeriod)
        _budgetsByType.update { newBudgetsByType }
    }


    suspend fun saveBudgets() {
        saveBudgetsUseCase.execute(
            budgets = budgetsByType.value.concatenate()
        )
    }


    private fun List<Budget>.replaceById(budgetDraft: BudgetDraft): List<Budget> {
        return this.map { budget ->
            budget.takeUnless { it.id == budgetDraft.id }
                ?: budgetDraft.copyDataToBudget(budget)
        }
    }

}