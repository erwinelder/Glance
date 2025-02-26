package com.ataglance.walletglance.account.data.remote.dao

import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter

class AccountRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<AccountRemoteEntity>
) {

    suspend fun upsertAccounts(accounts: List<AccountRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = accounts, userId = userId)
    }

    suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSync<AccountRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(
            toDelete = accountsToSync.toDelete, toUpsert = accountsToSync.toUpsert, userId = userId
        )
    }

    suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<AccountRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}