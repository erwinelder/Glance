package com.ataglance.walletglance.data.accounts

data class AccountsUiState(
    val accountList: List<Account> = emptyList(),
    val activeAccount: Account? = null
)