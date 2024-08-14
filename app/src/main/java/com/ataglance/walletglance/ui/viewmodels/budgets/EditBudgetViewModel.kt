package com.ataglance.walletglance.ui.viewmodels.budgets

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.budgets.EditingBudgetUiState
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.date.RepeatingPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditBudgetViewModel : ViewModel() {

    private val _budget: MutableStateFlow<EditingBudgetUiState> =
        MutableStateFlow(EditingBudgetUiState())
    val budget: StateFlow<EditingBudgetUiState> = _budget.asStateFlow()

    fun applyBudget(
        budget: EditingBudgetUiState?,
        category: Category? = null,
        newBudgetName: String = ""
    ) {
        _budget.update {
            budget ?: EditingBudgetUiState(
                isNew = true,
                category = category,
                name = newBudgetName
            )
        }
    }

    fun changeRepeatingPeriod(repeatingPeriod: RepeatingPeriod) {
        _budget.update {
            it.copy(repeatingPeriod = repeatingPeriod)
        }
    }

    fun changeCategory(category: Category) {
        _budget.update {
            it.copy(category = category)
        }
    }

    fun changeAmountLimit(value: String) {
        _budget.update {
            it.copy(amountLimit = value)
        }
    }

    fun changeName(value: String) {
        _budget.update {
            it.copy(name = value)
        }
    }

    fun linkWithAccount(account: Account) {
        val newList = budget.value.linkedAccounts.toMutableList()
        if (!newList.contains(account)) {
            newList.add(account)
        }

        _budget.update {
            it.copy(linkedAccounts = newList)
        }
    }

    fun unlinkWithAccount(account: Account) {
        val newList = budget.value.linkedAccounts.filter { it.id != account.id }

        _budget.update {
            it.copy(linkedAccounts = newList)
        }
    }

    fun getBudgetUiState(): EditingBudgetUiState {
        return budget.value
    }

}