package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.model.AccountCommandDto
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto

interface AccountRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        userId: Int
    ): List<AccountQueryDto>?

    suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<AccountQueryDto>?

    suspend fun getAccountsAfterTimestamp(timestamp: Long, userId: Int): List<AccountQueryDto>

}