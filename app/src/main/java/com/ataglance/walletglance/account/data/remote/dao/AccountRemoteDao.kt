package com.ataglance.walletglance.account.data.remote.dao

import com.ataglance.walletglance.account.data.mapper.toAccountRemoteEntity
import com.ataglance.walletglance.account.data.mapper.toMap
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AccountRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<AccountRemoteEntity>
) {

    suspend fun upsertEntities(entities: List<AccountRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = entities, userId = userId)
    }

    suspend fun synchroniseEntities(
        entitiesToSync: EntitiesToSynchronise<AccountRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(entitiesToSync = entitiesToSync, userId = userId)
    }

    suspend fun getEntitiesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSynchronise<AccountRemoteEntity> {
        return firestoreAdapter.getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}


fun accountRemoteDaoFactory(firestore: FirebaseFirestore): AccountRemoteDao {
    return AccountRemoteDao(
        firestoreAdapter = FirestoreAdapter(
            firestore = firestore,
            collectionName = TableName.Account.name,
            dataToEntityMapper = Map<String, Any?>::toAccountRemoteEntity,
            entityToDataMapper = AccountRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    )
}