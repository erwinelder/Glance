package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource?
) : AccountRepository {

    override suspend fun upsertAccounts(
        accountList: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.upsertEntities(entityList = accountList, timestamp = timestamp)
        remoteSource?.upsertEntities(entityList = accountList, timestamp = timestamp)
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
    ): Flow<List<AccountEntity>> = flow {
        try {

            val localTimestamp = localSource.getLastModifierTime()
            val remoteTimestamp = remoteSource?.getLastModifierTime() ?: localTimestamp

            if (remoteSource != null && remoteTimestamp > localTimestamp) {
                remoteSource.getEntitiesAfterTimestamp(localTimestamp)
                    .collect { entitiesToDeleteAndUpsert ->

                        localSource.deleteAndUpsertEntities(
                            entitiesToDelete = entitiesToDeleteAndUpsert.toDelete,
                            entitiesToUpsert = entitiesToDeleteAndUpsert.toUpsert,
                            timestamp = remoteTimestamp
                        )
                        localSource.getAllAccounts().collect { localList ->
                            emit(localList)
                            onSuccessListener()
                        }

                    }
            } else {
                localSource.getAllAccounts().collect { localList ->
                    emit(localList)
                    onSuccessListener()
                }
            }

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

}