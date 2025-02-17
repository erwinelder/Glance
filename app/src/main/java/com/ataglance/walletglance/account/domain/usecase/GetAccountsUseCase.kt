package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface GetAccountsUseCase {

    fun getAllFlow(): Flow<List<Account>>
    suspend fun getAll(): List<Account>

    suspend fun get(ids: List<Int>): List<Account>

    suspend fun get(id: Int): Account?

    suspend fun getActive(): Account?

}