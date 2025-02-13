package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.budget.toDraft
import com.ataglance.walletglance.budget.presentation.model.BudgetDraft
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCase
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.isPositiveNumberWithDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBudgetViewModel(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAccountsUseCase.getAll().let { accounts = it }
            getAllCategoriesUseCase.get().let { categoriesWithSubcategories = it }
        }
    }


    var accounts = emptyList<Account>()
        private set
    var categoriesWithSubcategories = CategoriesWithSubcategories()
        private set


    private val _budget = MutableStateFlow(BudgetDraft())
    val budget = _budget.asStateFlow()

    fun applyBudget(budget: Budget?) {
        val budgetDraft = budget?.toDraft(accounts = accounts) ?: let {
            val categoryWithSubcategory = categoriesWithSubcategories
                .getFirstCategoryWithSubcategoryByType(CategoryType.Expense)
            val category = categoryWithSubcategory?.category

            BudgetDraft(
                isNew = true,
                priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0,
                category = category,
                name = category?.name ?: ""
            )
        }

        _budget.update { budgetDraft }
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
        val finalValue = value.takeIf { it.isPositiveNumberWithDecimal() } ?: return

        _budget.update {
            it.copy(amountLimit = finalValue)
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

    fun getBudgetDraft(): BudgetDraft {
        return budget.value
    }

}