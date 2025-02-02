package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account

interface SaveAccountsUseCase {
    suspend fun execute(accountsToSave: List<Account>, currentAccounts: List<Account>)
}