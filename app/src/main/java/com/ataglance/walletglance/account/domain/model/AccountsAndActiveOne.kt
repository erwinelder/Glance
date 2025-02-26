package com.ataglance.walletglance.account.domain.model

data class AccountsAndActiveOne(
    val accounts: List<Account> = emptyList(),
    val activeAccount: Account? = null
) {

    companion object {

        fun fromAccounts(accounts: List<Account>): AccountsAndActiveOne {
            return AccountsAndActiveOne(
                accounts = accounts,
                activeAccount = accounts.firstOrNull { it.isActive }
            )
        }

    }

}