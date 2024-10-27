package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
            val localTimestamp = localSource.getUpdateTime()
            val remoteTimestamp = remoteSource?.getUpdateTime()

            if (remoteTimestamp != null && remoteTimestamp > localTimestamp) {
                val remoteList = suspendCoroutine { continuation ->
                    remoteSource?.getAllEntities(
                        onSuccessListener = { list -> continuation.resume(list) },
                        onFailureListener = onFailureListener
                    )
                }
                localSource.upsertEntities(entityList = remoteList, timestamp = remoteTimestamp)
                emit(remoteList)
                onSuccessListener()
            } else {
                emit(localSource.getAllAccounts().first())
            }
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

}