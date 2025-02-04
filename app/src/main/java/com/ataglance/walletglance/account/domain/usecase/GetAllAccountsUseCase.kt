package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface GetAllAccountsUseCase {
    fun getAsFlow(): Flow<List<Account>>
    suspend fun get(): List<Account>
}