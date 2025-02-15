package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.domain.model.Account

interface SaveAccountsUseCase {

    suspend fun saveDomainModels(accounts: List<Account>)

    suspend fun saveDataModels(accounts: List<AccountEntity>)

}