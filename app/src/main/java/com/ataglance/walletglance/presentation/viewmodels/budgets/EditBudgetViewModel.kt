package com.ataglance.walletglance.presentation.viewmodels.budgets

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.domain.accounts.Account
import com.ataglance.walletglance.domain.budgets.EditingBudgetUiState
import com.ataglance.walletglance.domain.categories.CategoryWithSubcategory
import com.ataglance.walletglance.domain.date.RepeatingPeriod
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
        categoryWithSubcategory: CategoryWithSubcategory? = null,
        newBudgetName: String = ""
    ) {
        val category = categoryWithSubcategory?.category

        _budget.update {
            budget ?: EditingBudgetUiState(
                isNew = true,
                priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0,
                category = category,
                name = newBudgetName.takeIf { it.isNotBlank() } ?: category?.name ?: ""
            )
        }
    }

    fun changeRepeatingPeriod(repeatingPeriod: RepeatingPeriod) {
        _budget.update {
            it.copy(newRepeatingPeriod = repeatingPeriod)
        }
    }

    fun changeCategory(categoryWithSubcategory: CategoryWithSubcategory) {
        val category = categoryWithSubcategory.getSubcategoryOrCategory()
        _budget.update { budget ->
            budget.copy(
                priorityNum = categoryWithSubcategory.groupParentAndSubcategoryOrderNums(),
                category = category,
                name = budget.name.takeIf {
                    it.isNotBlank() && budget.name != budget.category?.name
                } ?: category.name
            )
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
        if (newList.contains(account)) return

        newList.add(account)

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