package com.ataglance.walletglance.data.accounts

import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.domain.utils.findById

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