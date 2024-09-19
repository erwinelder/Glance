package com.ataglance.walletglance.account.domain

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.account.utils.findById

data class AccountsUiState(
    val accountList: List<Account> = emptyList(),
    val activeAccount: Account? = null
) {

    fun getAccountById(id: Int): Account? {
        return accountList.findById(id)
    }

    fun filterByBudget(budget: Budget?): List<Account> {
        return accountList.filter { budget?.linkedAccountsIds?.contains(it.id) == true }
    }

}