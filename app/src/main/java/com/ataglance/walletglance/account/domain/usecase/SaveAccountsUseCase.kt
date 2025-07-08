package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.domain.model.Account

interface SaveAccountsUseCase {

    suspend fun saveAndDeleteRest(accounts: List<Account>)

    suspend fun save(accounts: List<AccountDataModel>)

    suspend fun save(account: AccountDataModel)

}