package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.model.AccountCommandDto
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto

interface AccountRemoteDataSource {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean

    suspend fun getAccountsAfterTimestamp(timestamp: Long, token: String): List<AccountQueryDto>?

    suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<AccountQueryDto>?

}