package com.ataglance.walletglance.account.domain.model

data class AccountsAndActiveOne(
    val accounts: List<Account> = emptyList(),
    val activeAccount: Account? = null
)
