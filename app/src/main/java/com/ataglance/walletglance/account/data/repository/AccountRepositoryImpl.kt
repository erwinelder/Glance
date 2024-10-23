package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.flow.Flow

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource? = null
) : AccountRepository {

    override suspend fun upsertAccounts(
        accountList: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.upsertAccounts(accountList = accountList, timestamp = timestamp)
        remoteSource?.upsertEntities(entityList = accountList, timestamp = timestamp)
    }

    override suspend fun deleteAccountsByIds(
        idList: List<Int>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAccountsByIds(idList = idList, timestamp = timestamp)
        remoteSource?.deleteAccountsByIds(idList = idList, timestamp = timestamp)
    }

    override suspend fun deleteAllAccounts(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllAccounts(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override fun getAllAccounts(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<AccountEntity>> {
        return localSource.getAllAccounts()
    }

}