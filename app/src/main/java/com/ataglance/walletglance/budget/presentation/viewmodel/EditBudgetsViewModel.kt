package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.budget.domain.Budget
import com.ataglance.walletglance.budget.domain.BudgetsByType
import com.ataglance.walletglance.budget.domain.EditingBudgetUiState
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.budget.utils.groupByType
import com.ataglance.walletglance.budget.utils.replaceById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditBudgetsViewModel(
    passedBudgetsByType: BudgetsByType
) : ViewModel() {

    private val _budgetsByType: MutableStateFlow<BudgetsByType> = MutableStateFlow(
        passedBudgetsByType
    )
    val budgetsByType: StateFlow<BudgetsByType> = _budgetsByType.asStateFlow()

    fun saveBudget(budgetUiState: EditingBudgetUiState) {
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

    fun getBudgetList(): List<Budget> {
        return budgetsByType.value.concatenate()
    }

}

data class EditBudgetsViewModelFactory(
    private val budgetsByType: BudgetsByType
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditBudgetsViewModel(budgetsByType) as T
    }
}