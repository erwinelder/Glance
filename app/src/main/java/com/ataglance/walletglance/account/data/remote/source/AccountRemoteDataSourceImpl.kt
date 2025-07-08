package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.model.AccountCommandDto
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto

class AccountRemoteDataSourceImpl() : AccountRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeAccounts(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        userId: Int
    ): List<AccountQueryDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeAccountsAndGetAfterTimestamp(
        accounts: List<AccountCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<AccountQueryDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<AccountQueryDto> {
        // TODO("Not yet implemented")
        return emptyList()
    }

}