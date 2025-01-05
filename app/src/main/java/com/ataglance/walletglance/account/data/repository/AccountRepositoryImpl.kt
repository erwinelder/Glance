package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class AccountRepositoryImpl(
    override val localSource: AccountLocalDataSource,
    override val remoteSource: AccountRemoteDataSource?
) : AccountRepository {

    override suspend fun deleteAllEntities() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllAccounts(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllAccounts(timestamp = timestamp)
    }

}