package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.domain.model.Account

interface SaveAccountsUseCase {

    suspend fun execute(accountsToSave: List<Account>, currentAccounts: List<Account>)

    suspend fun execute(accounts: List<Account>)

    suspend fun execute(accounts: List<AccountEntity>)

}