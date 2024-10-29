package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class AccountRepositoryImpl(
    override val localSource: AccountLocalDataSource,
    override val remoteSource: AccountRemoteDataSource?
) : AccountRepository {

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllAccounts(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp, onSuccessListener, onFailureListener)
    }

}