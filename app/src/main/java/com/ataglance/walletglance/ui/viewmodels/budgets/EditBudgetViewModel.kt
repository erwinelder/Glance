package com.ataglance.walletglance.ui.viewmodels.budgets

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetRepeatingPeriod
import com.ataglance.walletglance.data.budgets.EditingBudgetUiState
import com.ataglance.walletglance.data.categories.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditBudgetViewModel : ViewModel() {

    private val _budget: MutableStateFlow<EditingBudgetUiState> =
        MutableStateFlow(EditingBudgetUiState())
    val budget: StateFlow<EditingBudgetUiState> = _budget.asStateFlow()

    fun applyBudget(
        budget: Budget?,
        category: Category? = null,
        newBudgetName: String = ""
    ) {
        _budget.update {
            budget?.toBudgetUiState() ?: EditingBudgetUiState(
                category = category,
                name = newBudgetName
            )
        }
    }

    fun changeUsedAmount(value: String) {
        _budget.update {
            it.copy(usedAmount = value)
        }
    }

    fun changeAmountLimit(value: String) {
        _budget.update {
            it.copy(amountLimit = value)
        }
    }

    fun changeCategory(category: Category) {
        _budget.update {
            it.copy(category = category)
        }
    }

    fun changeName(value: String) {
        _budget.update {
            it.copy(name = value)
        }
    }

    fun changeRepeatingPeriod(repeatingPeriod: BudgetRepeatingPeriod) {
        _budget.update {
            it.copy(repeatingPeriod = repeatingPeriod)
        }
    }

    fun linkWithAccount(accountId: Int) {
        val newList = budget.value.linkedAccountsIds.toMutableList()
        if (!newList.contains(accountId)) {
            newList.add(accountId)
        }

        _budget.update {
            it.copy(linkedAccountsIds = newList)
        }
    }

    fun unlinkWithAccount(accountId: Int) {
        val newList = budget.value.linkedAccountsIds.toMutableList()
        newList.remove(accountId)

        _budget.update {
            it.copy(linkedAccountsIds = newList)
        }
    }

    fun getBudget(): Budget? {
        return budget.value.toBudget()
    }

}