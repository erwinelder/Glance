package com.ataglance.walletglance.ui.viewmodels.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.utils.replaceById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditBudgetsViewModel(
    passedBudgetList: List<Budget>
) : ViewModel() {

    private val _budgets: MutableStateFlow<List<Budget>> = MutableStateFlow(passedBudgetList)
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    fun saveBudget(budget: Budget) {
        _budgets.update {
            if (budget.id == 0) {
                val newList = it.toMutableList()
                newList.add(budget)
                newList
            } else {
                it.replaceById(budget)
            }
        }
    }

    fun getBudgetList(): List<Budget> {
        return budgets.value
    }

}

data class EditBudgetsViewModelFactory(
    private val budgetList: List<Budget>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditBudgetsViewModel(budgetList) as T
    }
}