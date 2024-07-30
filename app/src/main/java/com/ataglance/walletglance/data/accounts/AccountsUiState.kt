package com.ataglance.walletglance.data.accounts

import com.ataglance.walletglance.data.utils.findById

data class AccountsUiState(
    val accountList: List<Account> = emptyList(),
    val activeAccount: Account? = null
) {
    fun getAccountById(id: Int): Account? {
        return accountList.findById(id)
    }
}